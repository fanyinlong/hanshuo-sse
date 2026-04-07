package com.shenmei.data.sse.dataservice.ruledata;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.shenmei.data.common.core.redis.RedisCache;
import com.shenmei.data.common.utils.StringUtils;
import com.shenmei.data.common.utils.bean.BeanUtils;
import com.shenmei.data.sse.constant.MatchOrderQtyEnum;
import com.shenmei.data.sse.constant.MatchTypeEnum;
import com.shenmei.data.sse.constant.SecuritySubCategoryEnum;
import com.shenmei.data.sse.domain.*;
import com.shenmei.data.sse.dto.OrderQuantityResult;
import com.shenmei.data.sse.service.*;
import com.shenmei.data.sse.util.OrderIdGenerateByPbu;
import com.shenmei.data.sse.util.SmallTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author teabot
 * @description 创建订单
 * @date 2025年8月2日
 */
@Slf4j
@Service
public class CreateOrderInfoService {

    @Resource
    private ISseDataRuleInitService sseDataRuleInitService;
    @Resource
    private ISseDataRuleService sseDataRuleService;
    @Resource
    private ISseSecurityInfoService sseSecurityInfoService;
    @Resource
    private ISseOrderInfoService iSseOrderInfoService;

    @Resource
    private ISseRuleExecuteRecordService sseRuleExecuteRecordService;

    @Resource
    private OrderIdGenerateByPbu orderIdGenerateByPbu;

    @Resource
    OrderQuantityCalcService orderQuantityCalcService;

    @Autowired
    private RedisCache redisCache;


    // 消息头常量
    private static final String MSG_HEAD_CREATE = "58";        // 创建订单
    private static final String MSG_HEAD_CANCEL = "61";        // 取消订单

    /**
     * 线程本地存储当前日期，避免多线程下频繁调用 LocalDate.now()
     */
    private static final ThreadLocal<LocalDate> TODAY = ThreadLocal.withInitial(LocalDate::now);

    private static final Long BUYER_QUANTITY = 2000L;

    private static final int BATCH_INSERT_QUANTITY = 2000;

    private static final BigDecimal BASE_ORDER_NUMBER = new BigDecimal(100000);

    private static final BigDecimal PRICE_AMPLIFY = new BigDecimal(100000);

    private static final Long ORD_QUANTITY_AMPLIFY = 1000L;

    /**
     * 思路：主板和科创板分开统计
     * 1.根据规则配置，提取pbu和账户集合
     * 2.根据股票数量配置，查找set集合并分配股票，set集合笔数和比例分布
     * 3.成交比计算，如果大于撮合成功比例，按照1：10和1:2进行撮合
     */


    /**
     * 创建全量订单
     *
     * @param sseRuleExecuteRecord
     */
    public void createOrderInfo(SseRuleExecuteRecord sseRuleExecuteRecord) {
        // 记录开始时间
        LocalDateTime startTime = LocalDateTime.now();
        //如果是全量执行，则删除该规则已有订单
        if (sseRuleExecuteRecord.getOrderType().equals("0")) {
            //删除该规则已有订单
            SseOrderInfo sseOrderInfo = new SseOrderInfo();
            sseOrderInfo.setRuleId(sseRuleExecuteRecord.getRuleId());
            iSseOrderInfoService.deleteSseOrderInfoByRuleId(sseRuleExecuteRecord.getRuleId());
        }
        // 并行执行科创板和主板订单创建
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        //看有多个10万的倍数
        int orderNumber = sseRuleExecuteRecord.getOrderNumber().intValue();
        int multiple = orderNumber / BASE_ORDER_NUMBER.intValue();
        log.info("开始创建订单，订单总数：{}, 倍数：{}", orderNumber, multiple);
        try {
            for (int i = 0; i < multiple; i++) {
                log.info("开始执行第 {} 轮订单创建", i + 1);
                Future<?> kshFuture = executorService.submit(() -> createKshOrderInfo(sseRuleExecuteRecord));
                Future<?> ashFuture = executorService.submit(() -> createAshOrderInfo(sseRuleExecuteRecord));

                // 等待两个任务都完成
                try {
                    kshFuture.get();
                    ashFuture.get();
                    log.info("第 {} 轮订单创建完成", i + 1);
                } catch (InterruptedException | ExecutionException e) {
                    log.error("执行第 {} 轮订单创建任务时发生错误", i + 1, e);
                    Thread.currentThread().interrupt();
                }
            }
        } finally {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }


        // 记录结束时间并计算执行时长
        LocalDateTime endTime = LocalDateTime.now();

        sseRuleExecuteRecord.setExecuteStatus("0");
        sseRuleExecuteRecordService.updateSseRuleExecuteRecord(sseRuleExecuteRecord);
        // 记录执行时长（毫秒）
        long duration = java.time.Duration.between(startTime, endTime).toMillis();
        // 将毫秒转换为时分秒毫秒格式
        long hours = duration / 3600000;
        long minutes = (duration % 3600000) / 60000;
        long seconds = (duration % 60000) / 1000;
        long milliseconds = duration % 1000;
        String formattedDuration = String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds);
        log.info("订单创建完成，开始时间: {}, 结束时间: {}, 执行时长: {} 毫秒", startTime, endTime, formattedDuration);
    }


    /**
     * 创建科创订单
     * 获取用户和pbu列表
     * 获取科创股票，按set股票数量进行分配
     * set订单笔数 = 订单总数* set比例
     * 每个set集合生成的订单符合撮合比例和成绩比的订单
     */
    public void createKshOrderInfo(SseRuleExecuteRecord sseRuleExecuteRecord) {
        //规则
        SseDataRule sseDataRule = sseDataRuleService.selectSseDataRuleByPkId(sseRuleExecuteRecord.getRuleId());
        //规则初始化
        SseDataRuleInit sseDataRuleInit = sseDataRuleInitService.selectSseDataRuleInitByRuleId(sseRuleExecuteRecord.getRuleId());
        //订单总数与基础订单（100000）的比值
//        BigDecimal orderNumberRatio = totalOrderNumber.divide(BASE_ORDER_NUMBER, 8, RoundingMode.UP);
        //获取用户和pbu列表
        String pbuAccountList = sseDataRuleInit.getPbuAccountList();
        //申报数量 order_quantity，取不到值，默认100
        Long orderQuantity = StringUtils.nvl(sseDataRule.getOrderQuantity(), 100L) * ORD_QUANTITY_AMPLIFY;
        //获取各撮合方式的数量
        String matchOrderList = sseDataRuleInit.getMatchOrderList();
        JSONObject matchOrderListJson = JSONObject.parseObject(matchOrderList);
        //获取各撮合方式的比例{"01":62.2,"02":15,"03":matchOrderKshList,"04":5,"05":5.6,"06":1.9,"07":1,"08":0.3}
        String matchMethod = sseDataRule.getMatchMethod();
        JSONObject matchMethodJson = JSONObject.parseObject(matchMethod);

        //科创板股票数量 security_sub_category:{"ASH":{"ratio":91.2,"quantity":14},"KSH":{"ratio":8.8,"quantity":6}}
        int security_ksh_quantity = 0;
        BigDecimal security_ksh_ratio = BigDecimal.ZERO;
        String securitySubCategory = sseDataRule.getSecuritySubCategory();
        JSONObject securitySubCategoryJson = JSON.parseObject(securitySubCategory);
        if (securitySubCategoryJson.containsKey(SecuritySubCategoryEnum.KSH.getCode())) {
            security_ksh_quantity = securitySubCategoryJson.getJSONObject(SecuritySubCategoryEnum.KSH.getCode()).getIntValue("quantity");
            security_ksh_ratio = securitySubCategoryJson.getJSONObject(SecuritySubCategoryEnum.KSH.getCode()).getBigDecimal("ratio");
        }
        log.info("科创板股票数量: {},订单比例：{}", security_ksh_quantity, security_ksh_ratio);

        //科创板股票
        String ksh_security_list = sseDataRuleInit.getSecurityKshList();
        JSONObject ksh_security_list_json = new JSONObject();
        if (StringUtils.isNotBlank(ksh_security_list)) {
            ksh_security_list_json = JSON.parseObject(ksh_security_list);
        }

        if (security_ksh_quantity > 0) {

            //获取set
            String kshSetConfig = sseDataRuleInit.getSetKshSecurity();
            //set存储：{"1":{"quantity":1,"orderNumber":1394,"ratio":1.39392000},"2":{"quantity":1,"orderNumber":1614,"ratio":1.61392000},"3":{"quantity":1,"orderNumber":1230,"ratio":1.22936000},"4":{"quantity":1,"orderNumber":866,"ratio":0.86504000},"5":{"quantity":1,"orderNumber":2056,"ratio":2.05832000},"6":{"quantity":1,"orderNumber":1640,"ratio":1.63944000}}
            if (StringUtils.isNotBlank(kshSetConfig)) {
                JSONObject kshSetJsonObject = JSON.parseObject(kshSetConfig);
                // 创建线程池处理SET集合
                ExecutorService executorService = Executors.newFixedThreadPool(Math.min(kshSetJsonObject.keySet().size(), 10));
                List<Future<?>> futures = new ArrayList<>();
                final long sseRuleExecuteRecordId= sseRuleExecuteRecord.getPkId();
                //循环处理每个SET集合
                for (String setId : kshSetJsonObject.keySet()) {
                    final String finalSetId = setId;
                    JSONObject finalKsh_security_list_json = ksh_security_list_json;
                    Future<?> future = executorService.submit(() -> {
                        try {
                            JSONObject setInfo = kshSetJsonObject.getJSONObject(setId);
                            //股票数量
                            int quantity = setInfo.getIntValue("quantity");
                            //当前set比例
                            BigDecimal ratio = setInfo.getBigDecimal("ratio");
                            //计算SET订单数
                            BigDecimal setOrderNumber = setInfo.getBigDecimal("orderNumber");

                            //按set股票数量进行分配
                            log.info("SET {} 分配 - 股票数量: {}, 比例: {}%, 订单数: {}",
                                    setId, quantity, ratio, setOrderNumber);

                            //随机获取科创股票
                            JSONArray ksh_security_jsonJSONArray = finalKsh_security_list_json.getJSONArray(setId);

                            //订单公共信息
                            SseOrderInfo sseOrderInfo = defaultOrderInfo(sseRuleExecuteRecord.getRuleId(), orderQuantity, setId);

                            // 使用封装的订单数量计算器
                            OrderQuantityResult orderQuantityResult =
                                    orderQuantityCalcService.calculateOrderQuantities(MatchTypeEnum.KSH, setOrderNumber, matchMethodJson, setId);

                            int invalidOrderNumber = orderQuantityResult.getInvalidOrderNumber();
                            int noMatchOrderNumber = orderQuantityResult.getNoMatchOrderNumber();
                            int cancelOrderNumber = orderQuantityResult.getCancelOrderNumber();
                            int matchOrderNumber = orderQuantityResult.getMatchOrderNumber();
                            int matchOrderBuyerNumber = matchOrderNumber / 2;


                            log.info("set:{},总订单数: {}", setId, setOrderNumber);
                            log.info("set:{},撤单笔数: {}", setId, cancelOrderNumber);
                            log.info("set:{},废单笔数: {}", setId, invalidOrderNumber);
                            log.info("set:{},不撮合订单: {}", setId, noMatchOrderNumber);
                            log.info("set:{},撮合订单买单: {}", setId, matchOrderBuyerNumber);


                            // 处理各类订单
                            processInvalidOrders(sseOrderInfo, pbuAccountList, ksh_security_jsonJSONArray, MatchTypeEnum.KSH, invalidOrderNumber, setId,sseRuleExecuteRecordId);
                            processNoMatchOrders(sseOrderInfo, pbuAccountList, ksh_security_jsonJSONArray, MatchTypeEnum.KSH, noMatchOrderNumber, setId,sseRuleExecuteRecordId);
                            processCancelOrders(sseOrderInfo, pbuAccountList, ksh_security_jsonJSONArray, MatchTypeEnum.KSH, cancelOrderNumber, setId,sseRuleExecuteRecordId);
                            processMatchOrders(sseOrderInfo, pbuAccountList, ksh_security_jsonJSONArray, MatchTypeEnum.KSH, matchOrderBuyerNumber, setId,sseRuleExecuteRecordId);
                        } catch (Exception e) {
                            log.error("处理SET {} 时发生错误", finalSetId, e);
                        }
                    });
                    futures.add(future);
                }
                // 等待所有任务完成
                for (Future<?> future : futures) {
                    try {
                        future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        log.error("执行SET任务时发生错误", e);
                    }
                }

                // 关闭线程池
                executorService.shutdown();
                try {
                    if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                        executorService.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    executorService.shutdownNow();
                }
            }
        }
    }

    /**
     * 创建主板订单
     */
    public void createAshOrderInfo(SseRuleExecuteRecord sseRuleExecuteRecord) {
        //规则
        SseDataRule sseDataRule = sseDataRuleService.selectSseDataRuleByPkId(sseRuleExecuteRecord.getRuleId());
        //规则初始化
        SseDataRuleInit sseDataRuleInit = sseDataRuleInitService.selectSseDataRuleInitByRuleId(sseRuleExecuteRecord.getRuleId());
//        //订单总数
//        BigDecimal totalOrderNumber = new BigDecimal(sseRuleExecuteRecord.getOrderNumber());
//        //订单总数与基础订单（100000）的比值
//        BigDecimal orderNumberRatio = totalOrderNumber.divide(BASE_ORDER_NUMBER, 8, RoundingMode.UP);
        //获取用户和pbu列表
        String pbuAccountList = sseDataRuleInit.getPbuAccountList();
        //申报数量 order_quantity，取不到值，默认100
        Long orderQuantity = StringUtils.nvl(sseDataRule.getOrderQuantity(), 100L) * ORD_QUANTITY_AMPLIFY;

        //获取各撮合方式的比例{"01":62.2,"02":15,"03":9,"04":5,"05":5.6,"06":1.9,"07":1,"08":0.3}
        String matchMethod = sseDataRule.getMatchMethod();
        JSONObject matchMethodJson = JSONObject.parseObject(matchMethod);

        //获取主板股票
        String ash_security_list = sseDataRuleInit.getSecurityAshList();
        JSONObject ash_security_list_json = new JSONObject();
        if (StringUtils.isNotBlank(ash_security_list)) {
            ash_security_list_json = JSON.parseObject(ash_security_list);
        }

        //为了成交比的计算，获取1撮10订单和1撮2订单的数量
        String matchOrderList = sseDataRuleInit.getMatchOrderList();
        JSONObject matchOrderListJson = JSONObject.parseObject(matchOrderList);
        //1撮10订单的买单数
        BigDecimal matching1v10Buyer = matchOrderListJson.getBigDecimal("matching1v10Buyer");
        //1撮2订单的买单数
        BigDecimal matching1v2Buyer = matchOrderListJson.getBigDecimal("matching1v2Buyer");

        //1撮10订单分配买单数
        AtomicInteger matching1v10Buyer_assigned = new AtomicInteger();
        //1撮2订单分配买单数
        AtomicInteger matching1v2Buyer_assigned = new AtomicInteger();

        //撮合成功订单数
        BigDecimal matching1v1Buyer = matchOrderListJson.getBigDecimal("matching1v1Buyer");

        //主板股票数量
        int security_ash_quantity = 0;
        //主板股票占比
        BigDecimal ashRatio;
        // security_sub_category:{"ASH":{"ratio":91.2,"quantity":14},"KSH":{"ratio":8.8,"quantity":6}}
        String securitySubCategory = sseDataRule.getSecuritySubCategory();
        JSONObject securitySubCategoryJson = JSON.parseObject(securitySubCategory);
        if (securitySubCategoryJson.containsKey(SecuritySubCategoryEnum.ASH.getCode())) {
            security_ash_quantity = securitySubCategoryJson.getJSONObject(SecuritySubCategoryEnum.ASH.getCode()).getIntValue("quantity");
            ashRatio = securitySubCategoryJson.getJSONObject(SecuritySubCategoryEnum.ASH.getCode()).getBigDecimal("ratio");
        } else {
            ashRatio = BigDecimal.ZERO;
        }

        log.info("主板股票数量: {}", security_ash_quantity);

        if (security_ash_quantity > 0) {
            final long sseRuleExecuteRecordId= sseRuleExecuteRecord.getPkId();
            //获取set
            String ashSetConfig = sseDataRuleInit.getSetAshSecurity();
            //set存储：{"1":{"quantity":2,"ratio":14.12749718},"2":{"quantity":2,"ratio":16.35721581},"3":{"quantity":2,"ratio":12.45966766},"4":{"quantity":4,"ratio":10.77850877},"5":{"quantity":2,"ratio":20.86124742},"6":{"quantity":2,"ratio":16.61586317}}
            if (StringUtils.isNotBlank(ashSetConfig)) {

                JSONObject ashSetJsonObject = JSON.parseObject(ashSetConfig);
                // 创建线程池处理SET集合
                ExecutorService executorService = Executors.newFixedThreadPool(Math.min(ashSetJsonObject.keySet().size(), 10));
                List<Future<?>> futures = new ArrayList<>();
                //循环处理每个SET集合
                for (String setId : ashSetJsonObject.keySet()) {
                    final String finalSetId = setId;
                    JSONObject finalAsh_security_list_json = ash_security_list_json;
                    Future<?> future = executorService.submit(() -> {
                        try {
                            JSONObject setInfo = ashSetJsonObject.getJSONObject(setId);
                            //股票数量
                            int quantity = setInfo.getIntValue("quantity");
                            BigDecimal ratio = setInfo.getBigDecimal("ratio");
                            //计算SET订单数
                            BigDecimal setOrderNumber = setInfo.getBigDecimal("orderNumber");
//
//                    //计算SET订单数 set订单笔数 = 订单总数* set比例
//                    BigDecimal setOrderNumber = totalOrderNumber.multiply(ratio).divide(BigDecimal.valueOf(100), 8, RoundingMode.UP);
                            log.info("主板SET： {} 订单数: {}", setId, setOrderNumber);

                            //随机获取主板股票
                            JSONArray ash_security_jsonJSONArray = finalAsh_security_list_json.getJSONArray(setId);
                            //
                            log.info("SET {} 分配 - 股票数量: {}, 比例: {}%, 订单数: {}",
                                    setId, quantity, ratio, setOrderNumber);

                            //订单公共信息
                            SseOrderInfo sseOrderInfo = defaultOrderInfo(sseRuleExecuteRecord.getRuleId(), orderQuantity, setId);

                            // 使用封装的订单数量计算器
                            OrderQuantityResult orderQuantityResult =
                                    orderQuantityCalcService.calculateOrderQuantities(MatchTypeEnum.ASH, setOrderNumber, matchMethodJson, setId);

                            int invalidOrderNumber = orderQuantityResult.getInvalidOrderNumber();
                            int noMatchOrderNumber = orderQuantityResult.getNoMatchOrderNumber();
                            int cancelOrderNumber = orderQuantityResult.getCancelOrderNumber();

                            // 处理各类订单
                            processInvalidOrders(sseOrderInfo, pbuAccountList, ash_security_jsonJSONArray, MatchTypeEnum.ASH, invalidOrderNumber, setId,sseRuleExecuteRecordId);
                            processNoMatchOrders(sseOrderInfo, pbuAccountList, ash_security_jsonJSONArray, MatchTypeEnum.ASH, noMatchOrderNumber, setId,sseRuleExecuteRecordId);
                            processCancelOrders(sseOrderInfo, pbuAccountList, ash_security_jsonJSONArray, MatchTypeEnum.ASH, cancelOrderNumber, setId,sseRuleExecuteRecordId);


                            //生成撮合订单 1*1
                            //撮合成功总数，撮合成功比例
                            BigDecimal matchOrder_set = matching1v1Buyer.multiply(ratio).divide(ashRatio, 8, RoundingMode.UP);
                            int matchOrder_set_int = matchOrder_set.setScale(0, RoundingMode.HALF_UP).intValue();
                            log.info("set:{},撮合订单买单: {}", setId, matchOrder_set_int);
                            processMatchOrders(sseOrderInfo, pbuAccountList, ash_security_jsonJSONArray, MatchTypeEnum.ASH, matchOrder_set_int, setId,sseRuleExecuteRecordId);

                            //1撮10订单当前set数量
                            //计算方式：1撮10总数*，set比例占比（当前set占比/主板比例）
                            BigDecimal matching1v10Buyer_set = matching1v10Buyer.multiply(ratio).divide(ashRatio, 8, RoundingMode.UP);
                            int matching1v10Buyer_set_int = matching1v10Buyer_set.setScale(0, RoundingMode.HALF_UP).intValue();

                            log.info("set:{},1撮10订单当前set数量: {},精度：{}", setId, matching1v10Buyer_set_int, matching1v10Buyer_set);
                            matching1v10Buyer_assigned.set(matching1v10Buyer_assigned.get() + matching1v10Buyer_set_int);
                            processMatch1To10Orders(sseOrderInfo, pbuAccountList, ash_security_jsonJSONArray, MatchTypeEnum.ASH, matching1v10Buyer_set_int, setId,sseRuleExecuteRecordId);

                            //1撮2订单
                            //计算方式：1撮2总数*，set比例占比（当前set占比/主板比例）
                            BigDecimal matching1v2Buyer_set = matching1v2Buyer.multiply(ratio).divide(ashRatio, 8, RoundingMode.UP);
                            // 如果大于0小于1，则为1
                            int matching1v2Buyer_set_int = matching1v10Buyer_set.compareTo(BigDecimal.ZERO) > 0 && matching1v2Buyer_set.compareTo(BigDecimal.ONE) < 0 ? 1 : matching1v2Buyer_set.intValue();
                            log.info("set:{},1撮2订单当前set数量: {}", setId, matching1v2Buyer_set_int);
                            matching1v2Buyer_assigned.set(matching1v2Buyer_assigned.get() + matching1v2Buyer_set_int);
                            processMatch1To2Orders(sseOrderInfo, pbuAccountList, ash_security_jsonJSONArray, MatchTypeEnum.ASH, matching1v2Buyer_set_int, setId,sseRuleExecuteRecordId);
                            log.info("1撮10订单的买单数: {},已分配买单数：{}", matching1v10Buyer, matching1v10Buyer_assigned);
                            log.info("1撮2订单的买单数: {},已分配买单数：{}", matching1v2Buyer, matching1v2Buyer_assigned);


                        } catch (Exception e) {
                            log.error("处理主板SET {} 时发生错误", finalSetId, e);
                        }
                    });
                    futures.add(future);
                }
                // 等待所有任务完成
                for (Future<?> future : futures) {
                    try {
                        future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        log.error("执行主板SET任务时发生错误", e);
                    }
                }

                // 关闭线程池
                executorService.shutdown();
                try {
                    if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                        executorService.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    executorService.shutdownNow();
                }

            }
        }

    }


    /**
     * 根据账户列表随机选择一个账户
     *
     * @param pbuAccountList
     * @return
     */
    private Map<String, String> randomPbuAccount(String pbuAccountList) {

        // 解析pbuAccountList JSON数组
        JSONArray pbuAccountArray = JSON.parseArray(pbuAccountList);

        // 随机选择一个pbuAccount对象
        int randomPbuAccountIndex = new Random().nextInt(pbuAccountArray.size());
        JSONObject selectedPbuAccount = pbuAccountArray.getJSONObject(randomPbuAccountIndex);

        // 获取loginPbu
        String loginPbu = selectedPbuAccount.getString("loginPbu");

        // 获取bizPbuAccount数组
        JSONArray bizPbuAccountArray = selectedPbuAccount.getJSONArray("bizPbuAccount");

        // 随机选择一个bizPbuAccount对象（检查数组是否为空）
        int randomBizPbuAccountIndex = bizPbuAccountArray != null && !bizPbuAccountArray.isEmpty()
                ? ThreadLocalRandom.current().nextInt(bizPbuAccountArray.size()) : 0;

        JSONObject selectedBizPbuAccount = bizPbuAccountArray != null && !bizPbuAccountArray.isEmpty()
                ? bizPbuAccountArray.getJSONObject(randomBizPbuAccountIndex) : selectedPbuAccount;


        // 获取bizPbu
        String bizPbu = selectedBizPbuAccount.getString("bizPbu");

        // 获取account数组
        JSONArray accountArray = selectedBizPbuAccount.getJSONArray("account");

        // 随机选择一个account
        int randomAccountIndex = new Random().nextInt(accountArray.size());
        String account = accountArray.getString(randomAccountIndex);

        // 构造返回的Map
        Map<String, String> result = new HashMap<>();
        result.put("loginPbu", loginPbu);
        result.put("bizPbu", bizPbu);
        result.put("account", account);
//        log.info("随机PbuAccount组合:{}",result);
        return result;
    }


    /**
     * 创建基础订单信息
     */
    private SseOrderInfo createBaseOrderInfo(SseOrderInfo baseOrder, String pbuAccountList) {
        SseOrderInfo orderInfo = new SseOrderInfo();
        BeanUtils.copyProperties(baseOrder, orderInfo);

        Map<String, String> pbuAccountMap = randomPbuAccount(pbuAccountList);
        orderInfo.setBizPbu(pbuAccountMap.get("bizPbu"));
        orderInfo.setAccount(pbuAccountMap.get("account"));
        orderInfo.setOrderTable("MTP_67_" + pbuAccountMap.get("loginPbu"));
        orderInfo.setOrdType("2"); // 限价
//        orderInfo.setClOrdId(orderIdGenerateByPbu.generateOrderNo(pbuAccountMap.get("loginPbu")));
        //按bizPbu生成订单号
        orderInfo.setClOrdId(orderIdGenerateByPbu.generateOrderNo(pbuAccountMap.get("bizPbu")));
        return orderInfo;
    }

    /**
     * 随机股票的股票代码和收盘价
     */
    private SseSecurityInfo randomSecurityInfo(JSONArray jsonArray) {
        int randomIndex = ThreadLocalRandom.current().nextInt(jsonArray.size());
        SseSecurityInfo sseSecurityInfo = new SseSecurityInfo();
        sseSecurityInfo.setSecurityId(jsonArray.getJSONObject(randomIndex).getString("securityId"));
        sseSecurityInfo.setPriorClosingPrice(jsonArray.getJSONObject(randomIndex).getDouble("priorClosingPrice"));
        return sseSecurityInfo;
    }


    /**
     * 生成废单
     */
    private SseOrderInfo generateInvalidOrder(SseOrderInfo sseOrderInfo, String pbuAccountList, JSONArray
            security_jsonJSONArray, MatchTypeEnum matchTypeEnum) {
        SseOrderInfo orderInfo = createBaseOrderInfo(sseOrderInfo, pbuAccountList);
        orderInfo.setSide("1");
        orderInfo.setPrice(BigDecimal.ZERO);
        orderInfo.setOrderMatching(matchTypeEnum.getInvalidKey());
        // 从securityInfoList中随机选择一个securityId（支持多线程）
        SseSecurityInfo sseSecurityInfo = randomSecurityInfo(security_jsonJSONArray);
        orderInfo.setSecurityId(sseSecurityInfo.getSecurityId());
        return orderInfo;
    }


    /**
     * 生成撤单订单-原单和撤单
     */
    private List<SseOrderInfo> generateCancelOrder(SseOrderInfo baseOrder, String pbuAccountList, JSONArray
            security_jsonJSONArray, MatchTypeEnum matchTypeEnum) {
        List<SseOrderInfo> cancelOrders = new ArrayList<>();
        // 创建原始订单
        SseOrderInfo orderInfo = createBaseOrderInfo(baseOrder, pbuAccountList);
        SseSecurityInfo securityInfo = randomSecurityInfo(security_jsonJSONArray);
        BigDecimal closingPrice = BigDecimal.valueOf(securityInfo.getPriorClosingPrice());

        orderInfo.setSecurityId(securityInfo.getSecurityId());
        orderInfo.setSide(SmallTools.setRamdomOrderSide());
        orderInfo.setOrderMatching(matchTypeEnum.getCancelKey());
        setOrderPrice(orderInfo, closingPrice);

        // 创建撤单订单
        SseOrderInfo cancelOrder = createCancelOrder(orderInfo);

        cancelOrders.add(orderInfo);
        cancelOrders.add(cancelOrder);
        return cancelOrders;
    }

    /**
     * 生成不撮合订单
     *
     * @param sseOrderInfo
     * @param pbuAccountList
     * @param security_jsonJSONArray
     * @param matchTypeEnum
     * @param index                  订单索引
     * @return
     */
    private SseOrderInfo generateNoMatchOrder(SseOrderInfo sseOrderInfo, String pbuAccountList, JSONArray
            security_jsonJSONArray, MatchTypeEnum matchTypeEnum, int index) {
        // 创建新订单并设置基础信息
        SseOrderInfo orderInfo = createBaseOrderInfo(sseOrderInfo, pbuAccountList);

        // 从securityInfoList中随机选择一个securityId（支持多线程）
        SseSecurityInfo securityInfo = randomSecurityInfo(security_jsonJSONArray);

        // 设置证券代码和收盘价
        orderInfo.setSecurityId(securityInfo.getSecurityId());
        BigDecimal closingPrice = BigDecimal.valueOf(securityInfo.getPriorClosingPrice());
        setLimitOrderPrice(orderInfo, closingPrice, index); // 设置超出撮合范围的价格
        orderInfo.setOrderMatching(matchTypeEnum.getNoMatchKey());
        return orderInfo;
    }


    /**
     * 生成撮合订单
     */
    private List<SseOrderInfo> generateMatchOrder(SseOrderInfo baseOrder, String pbuAccountList, JSONArray
            security_jsonJSONArray, MatchTypeEnum matchTypeEnum, MatchOrderQtyEnum matchOrderQtyEnum) {
        List<SseOrderInfo> matchOrders = new ArrayList<>();

        SseOrderInfo orderInfo = createBaseOrderInfo(baseOrder, pbuAccountList);
        SseSecurityInfo securityInfo = randomSecurityInfo(security_jsonJSONArray);
        BigDecimal closingPrice = BigDecimal.valueOf(securityInfo.getPriorClosingPrice()).multiply(PRICE_AMPLIFY);
        orderInfo.setPrice(closingPrice);
        orderInfo.setSecurityId(securityInfo.getSecurityId());
        orderInfo.setSide("1");
        orderInfo.setOrderMatching(matchTypeEnum.getMatchKey());
        //如果1撮多，则申报数量单独设置
        if (matchOrderQtyEnum.getMatchOrderQty() > 1) {
            orderInfo.setOrderQty(BUYER_QUANTITY * ORD_QUANTITY_AMPLIFY);
        }
        matchOrders.add(orderInfo);
        // 根据撮合比例，添加撮合订单
        for (int i = 0; i < matchOrderQtyEnum.getMatchOrderQty(); i++) {
            SseOrderInfo orderInfoMatch = createBaseOrderInfo(baseOrder, pbuAccountList);
            orderInfoMatch.setSide("2");
            orderInfoMatch.setPrice(orderInfo.getPrice());
            orderInfoMatch.setSecurityId(orderInfo.getSecurityId());
            orderInfoMatch.setOrderMatching(orderInfo.getOrderMatching());
            orderInfoMatch.setOrderQty(orderInfo.getOrderQty() / matchOrderQtyEnum.getMatchOrderQty());
            matchOrders.add(orderInfoMatch);
        }
//        log.info("生成{}个订单", matchOrderQtyEnum.getMatchOrderQty());
        return matchOrders;
    }


    /**
     * 创建撤单订单
     */
    private SseOrderInfo createCancelOrder(SseOrderInfo originalOrder) {
        SseOrderInfo cancelOrder = new SseOrderInfo();
        BeanUtils.copyProperties(originalOrder, cancelOrder);
        cancelOrder.setMsgHead(MSG_HEAD_CANCEL);
        cancelOrder.setOrigClOrdId(originalOrder.getClOrdId());
        cancelOrder.setClOrdId(orderIdGenerateByPbu.generateOrderNo(cancelOrder.getBizPbu()));
        return cancelOrder;
    }

    /**
     * 设置常规订单价格
     */
    private void setOrderPrice(SseOrderInfo order, BigDecimal closingPrice) {
        if (order.getSide().equals("1")) {
            order.setPrice(closingPrice.multiply(BigDecimal.valueOf(0.95))
                    .setScale(2, RoundingMode.HALF_UP).multiply(PRICE_AMPLIFY));
        } else {
            order.setPrice(closingPrice.multiply(BigDecimal.valueOf(1.05))
                    .setScale(2, RoundingMode.HALF_UP).multiply(PRICE_AMPLIFY));
        }
    }

    /**
     * 设置超出撮合范围的价格（上/下五档）
     */
    private void setLimitOrderPrice(SseOrderInfo order, BigDecimal closingPrice, int index) {
        if (index % 2 == 0) {
            // 买方订单：收盘价-0.5% 到 -2.5%
            BigDecimal buyPrice = closingPrice.multiply(BigDecimal.valueOf(1.0 - (0.005 * ((index % 5) + 1))))
                    .setScale(2, RoundingMode.HALF_UP).multiply(PRICE_AMPLIFY);
            order.setPrice(buyPrice);
            order.setSide("1");
        } else {
            // 卖方订单：收盘价+0.5% 到 +2.5%
            BigDecimal sellPrice = closingPrice.multiply(BigDecimal.valueOf(1.0 + (0.005 * ((index % 5) + 1))))
                    .setScale(2, RoundingMode.HALF_UP).multiply(PRICE_AMPLIFY);
            order.setPrice(sellPrice);
            order.setSide("2");
        }
    }

    //订单公共信息-默认
    public SseOrderInfo defaultOrderInfo(Long ruleId, Long orderQuantity, String setId) {
        SseOrderInfo sseOrderInfo = new SseOrderInfo();
        sseOrderInfo.setRuleId(ruleId);
        //固定数据-poc
        sseOrderInfo.setMsgHead(MSG_HEAD_CREATE);
        sseOrderInfo.setBizId(100010);
        sseOrderInfo.setTimeInForce("0");
        sseOrderInfo.setBranchId("50001");
        sseOrderInfo.setOrderQty(orderQuantity);
        sseOrderInfo.setOrderSet(setId);


        // 设置交易时间为当天10:00:00
        LocalTime tenOClock = LocalTime.of(10, 0, 0);
        LocalDate today = TODAY.get();
        LocalDateTime todayTen = LocalDateTime.of(today, tenOClock);
        sseOrderInfo.setTransactTime(Date.from(todayTen.atZone(ZoneId.systemDefault()).toInstant()));
        return sseOrderInfo;
    }

    /**
     * 处理废单
     */
    private void processInvalidOrders(SseOrderInfo baseOrder, String pbuAccountList,
                                      JSONArray securityJsonArray, MatchTypeEnum matchTypeEnum,
                                      int orderCount, String setId,long sseRuleExecuteRecordId) {
        List<SseOrderInfo> orders = new ArrayList<>();
        for (int i = 0; i < orderCount; i++) {
            SseOrderInfo orderInfo = generateInvalidOrder(baseOrder, pbuAccountList, securityJsonArray, matchTypeEnum);
            orders.add(orderInfo);

            // 批量插入阈值检查
            if (orders.size() >= BATCH_INSERT_QUANTITY) {
                batchInsertOrders(orders, setId, "废单",sseRuleExecuteRecordId);
                orders = new ArrayList<>();
            }
        }
        // 插入剩余订单
        batchInsertOrders(orders, setId, "废单",sseRuleExecuteRecordId);
    }

    /**
     * 处理不撮合订单
     */
    private void processNoMatchOrders(SseOrderInfo baseOrder, String pbuAccountList,
                                      JSONArray securityJsonArray, MatchTypeEnum matchTypeEnum,
                                      int orderCount, String setId,long sseRuleExecuteRecordId) {
        List<SseOrderInfo> orders = new ArrayList<>();
        for (int i = 0; i < orderCount; i++) {
            SseOrderInfo orderInfo = generateNoMatchOrder(baseOrder, pbuAccountList, securityJsonArray, matchTypeEnum, i);
            orders.add(orderInfo);

            // 批量插入阈值检查
            if (orders.size() >= BATCH_INSERT_QUANTITY) {
                batchInsertOrders(orders, setId, "不撮合",sseRuleExecuteRecordId);
                orders = new ArrayList<>();
            }
        }
        // 插入剩余订单
        batchInsertOrders(orders, setId, "不撮合",sseRuleExecuteRecordId);
    }

    /**
     * 处理撤单
     */
    private void processCancelOrders(SseOrderInfo baseOrder, String pbuAccountList,
                                     JSONArray securityJsonArray, MatchTypeEnum matchTypeEnum,
                                         int orderCount, String setId,long sseRuleExecuteRecordId) {
        List<SseOrderInfo> orders = new ArrayList<>();
        // 撤单需要成对生成（原单+撤单）
        for (int i = 0; i < orderCount / 2; i++) {
            List<SseOrderInfo> cancelOrderList = generateCancelOrder(baseOrder, pbuAccountList, securityJsonArray, matchTypeEnum);
            orders.addAll(cancelOrderList);

            // 批量插入阈值检查
            if (orders.size() >= BATCH_INSERT_QUANTITY) {
                batchInsertOrders(orders, setId, "撤单",sseRuleExecuteRecordId);
                orders = new ArrayList<>();
            }
        }
        // 插入剩余订单
        batchInsertOrders(orders, setId, "撤单",sseRuleExecuteRecordId);
    }

    /**
     * 处理撮合订单（1:1）
     */
    private void processMatchOrders(SseOrderInfo baseOrder, String pbuAccountList,
                                    JSONArray securityJsonArray, MatchTypeEnum matchTypeEnum,
                                    int buyerOrderCount, String setId,long sseRuleExecuteRecordId) {
        List<SseOrderInfo> orders = new ArrayList<>();
        for (int i = 0; i < buyerOrderCount; i++) {
            List<SseOrderInfo> matchOrderList = generateMatchOrder(baseOrder, pbuAccountList, securityJsonArray, matchTypeEnum, MatchOrderQtyEnum.MATCH_ORDER_QTY_ONE);
            orders.addAll(matchOrderList);

            // 批量插入阈值检查
            if (orders.size() >= BATCH_INSERT_QUANTITY) {
                batchInsertOrders(orders, setId, "撮合",sseRuleExecuteRecordId);
                orders = new ArrayList<>();
            }
        }
        // 插入剩余订单
        batchInsertOrders(orders, setId, "撮合",sseRuleExecuteRecordId);
    }

    /**
     * 处理1:10撮合订单
     */
    private void processMatch1To10Orders(SseOrderInfo baseOrder, String pbuAccountList,
                                         JSONArray securityJsonArray, MatchTypeEnum matchTypeEnum,
                                         int buyerOrderCount, String setId,long sseRuleExecuteRecordId) {
        List<SseOrderInfo> orders = new ArrayList<>();
        for (int i = 0; i < buyerOrderCount; i++) {
            List<SseOrderInfo> matchOrderList = generateMatchOrder(baseOrder, pbuAccountList, securityJsonArray, matchTypeEnum, MatchOrderQtyEnum.MATCH_ORDER_QTY_TEN);
            orders.addAll(matchOrderList);

            // 批量插入阈值检查
            if (orders.size() >= BATCH_INSERT_QUANTITY) {
                batchInsertOrders(orders, setId, "1:10撮合",sseRuleExecuteRecordId);
                orders = new ArrayList<>();
            }
        }
        // 插入剩余订单
        batchInsertOrders(orders, setId, "1:10撮合",sseRuleExecuteRecordId);
    }

    /**
     * 处理1:2撮合订单
     */
    private void processMatch1To2Orders(SseOrderInfo baseOrder, String pbuAccountList,
                                        JSONArray securityJsonArray, MatchTypeEnum matchTypeEnum,
                                        int buyerOrderCount, String setId,long sseRuleExecuteRecordId) {
        List<SseOrderInfo> orders = new ArrayList<>();
        for (int i = 0; i < buyerOrderCount; i++) {
            List<SseOrderInfo> matchOrderList = generateMatchOrder(baseOrder, pbuAccountList, securityJsonArray, matchTypeEnum, MatchOrderQtyEnum.MATCH_ORDER_QTY_TWO);
            orders.addAll(matchOrderList);

            // 批量插入阈值检查
            if (orders.size() >= BATCH_INSERT_QUANTITY) {
                batchInsertOrders(orders, setId, "1:2撮合",sseRuleExecuteRecordId);
                orders = new ArrayList<>();
            }
        }
        // 插入剩余订单
        batchInsertOrders(orders, setId, "1:2撮合",sseRuleExecuteRecordId);
    }

    /**
     * 批量插入订单的通用方法
     *
     * @param orders    订单列表
     * @param setId     SET编号
     * @param orderType 订单类型
     */
    private void batchInsertOrders(List<SseOrderInfo> orders, String setId, String orderType,long sseRuleExecuteRecordId) {
        if (!orders.isEmpty()) {
            iSseOrderInfoService.batchInsertSseOrderInfo(orders);
            log.info("SET {} 已批量插入{}个{}订单", setId, orders.size(), orderType);
            // 新增Redis缓存更新逻辑
            try {
                String cacheKey = "sseRuleExecuteRecord_generatedCount_" + sseRuleExecuteRecordId;
                redisCache.increment(cacheKey, orders.size());                // 可选：设置缓存过期时间，例如24小时
                redisCache.expire(cacheKey, 48, TimeUnit.HOURS);
            } catch (Exception e) {
                log.error("Redis缓存更新失败", e);
            }

        }

    }

}