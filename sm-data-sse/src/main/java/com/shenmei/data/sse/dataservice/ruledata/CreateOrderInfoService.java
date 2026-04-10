package com.shenmei.data.sse.dataservice.ruledata;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.shenmei.data.common.core.redis.RedisCache;
import com.shenmei.data.common.utils.StringUtils;
import com.shenmei.data.common.utils.bean.BeanUtils;
import com.shenmei.data.sse.config.RabbitMQConfig;
import com.shenmei.data.sse.constant.MatchOrderQtyEnum;
import com.shenmei.data.sse.constant.MatchTypeEnum;
import com.shenmei.data.sse.constant.SecuritySubCategoryEnum;
import com.shenmei.data.sse.domain.*;
import com.shenmei.data.sse.dto.OrderCompletedMessage;
import com.shenmei.data.sse.dto.OrderQuantityResult;
import com.shenmei.data.sse.mq.OrderCompletedProducer;
import com.shenmei.data.sse.service.*;
import com.shenmei.data.sse.util.OrderIdGenerateByPbu;
import com.shenmei.data.sse.util.SmallTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class CreateOrderInfoService {

    @Resource
    private ISseDataRuleInitService sseDataRuleInitService;
    @Resource
    private ISseDataRuleService sseDataRuleService;
    @Resource
    private ISseOrderInfoService iSseOrderInfoService;
    @Resource
    private ISseRuleExecuteRecordService sseRuleExecuteRecordService;
    @Resource
    private OrderIdGenerateByPbu orderIdGenerateByPbu;
    @Resource
    OrderQuantityCalcService orderQuantityCalcService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Resource
    private OrderCompletedProducer orderCompletedProducer;
    @Autowired
    private RedisCache redisCache;

    private static final String MSG_HEAD_CREATE = "58";
    private static final String MSG_HEAD_CANCEL = "61";
    private static final ThreadLocal<LocalDate> TODAY = ThreadLocal.withInitial(LocalDate::now);
    private static final Long BUYER_QUANTITY = 2000L;
    private static final int BATCH_SEND_MQ_QUANTITY = 2000;
    private static final BigDecimal BASE_ORDER_NUMBER = new BigDecimal(100000);
    private static final BigDecimal PRICE_AMPLIFY = new BigDecimal(100000);
    private static final Long ORD_QUANTITY_AMPLIFY = 1000L;

    public void createOrderInfo(SseRuleExecuteRecord sseRuleExecuteRecord) {
        LocalDateTime startTime = LocalDateTime.now();
        if ("0".equals(sseRuleExecuteRecord.getOrderType())) {
            iSseOrderInfoService.deleteSseOrderInfoByRuleId(sseRuleExecuteRecord.getRuleId());
        }

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        int orderNumber = sseRuleExecuteRecord.getOrderNumber().intValue();
        int multiple = orderNumber / BASE_ORDER_NUMBER.intValue();
        log.info("开始创建订单，订单总数：{}, 倍数：{}", orderNumber, multiple);

        AtomicLong totalGeneratedCount = new AtomicLong(0);

        try {
            for (int i = 0; i < multiple; i++) {
                log.info("开始执行第 {} 轮订单创建", i + 1);
                CountDownLatch latch = new CountDownLatch(2);
                AtomicLong kshCount = new AtomicLong(0);
                AtomicLong ashCount = new AtomicLong(0);

                executorService.submit(() -> {
                    try {
                        kshCount.set(createKshOrderInfo(sseRuleExecuteRecord));
                    } finally {
                        latch.countDown();
                    }
                });

                executorService.submit(() -> {
                    try {
                        ashCount.set(createAshOrderInfo(sseRuleExecuteRecord));
                    } finally {
                        latch.countDown();
                    }
                });

                try {
                    latch.await(60, TimeUnit.MINUTES);
                    long roundCount = kshCount.get() + ashCount.get();
                    totalGeneratedCount.addAndGet(roundCount);
                    log.info("第 {} 轮完成，生成: {} 条", i + 1, roundCount);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        } finally {
            executorService.shutdown();
        }

        LocalDateTime endTime = LocalDateTime.now();
        sseRuleExecuteRecord.setExecuteStatus("0");
        sseRuleExecuteRecord.setOrderNumber(totalGeneratedCount.get());
        sseRuleExecuteRecordService.updateSseRuleExecuteRecord(sseRuleExecuteRecord);

        long duration = java.time.Duration.between(startTime, endTime).toMillis();
        String formattedDuration = String.format("%02d:%02d:%02d.%03d",
                duration / 3600000, (duration % 3600000) / 60000, (duration % 60000) / 1000, duration % 1000);
        log.info("订单生成完成，耗时: {}, 总数: {}", formattedDuration, totalGeneratedCount.get());

        OrderCompletedMessage message = new OrderCompletedMessage();
        message.setRuleId(sseRuleExecuteRecord.getRuleId());
        message.setExecuteRecordId(sseRuleExecuteRecord.getPkId());
        message.setTotalOrderCount(totalGeneratedCount.get());
        message.setExecuteUser(sseRuleExecuteRecord.getCreateBy());
        message.setTimestamp(System.currentTimeMillis());
        orderCompletedProducer.sendOrderCompletedMessage(message);
    }

    public long createKshOrderInfo(SseRuleExecuteRecord record) {
        SseDataRule rule = sseDataRuleService.selectSseDataRuleByPkId(record.getRuleId());
        SseDataRuleInit init = sseDataRuleInitService.selectSseDataRuleInitByRuleId(record.getRuleId());
        String pbuAccountList = init.getPbuAccountList();
        Long orderQuantity = StringUtils.nvl(rule.getOrderQuantity(), 100L) * ORD_QUANTITY_AMPLIFY;
        JSONObject matchMethodJson = JSONObject.parseObject(rule.getMatchMethod());

        JSONObject securitySubCategoryJson = JSON.parseObject(rule.getSecuritySubCategory());
        if (!securitySubCategoryJson.containsKey(SecuritySubCategoryEnum.KSH.getCode())) return 0;
        int kshQuantity = securitySubCategoryJson.getJSONObject(SecuritySubCategoryEnum.KSH.getCode()).getIntValue("quantity");
        if (kshQuantity == 0) return 0;

        JSONObject kshSecurityJson = StringUtils.isNotBlank(init.getSecurityKshList())
                ? JSON.parseObject(init.getSecurityKshList()) : new JSONObject();
        JSONObject kshSetJson = StringUtils.isNotBlank(init.getSetKshSecurity())
                ? JSON.parseObject(init.getSetKshSecurity()) : new JSONObject();

        AtomicLong count = new AtomicLong(0);
        ExecutorService pool = Executors.newFixedThreadPool(Math.min(kshSetJson.size(), 15));
        List<Future<Long>> futures = new ArrayList<>();

        for (String setId : kshSetJson.keySet()) {
            final String finalSetId = setId;
            futures.add(pool.submit(() -> {
                try {
                    JSONObject setInfo = kshSetJson.getJSONObject(finalSetId);
                    JSONArray securities = kshSecurityJson.getJSONArray(finalSetId);
                    if (securities == null || securities.isEmpty()) return 0L;

                    SseOrderInfo baseOrder = defaultOrderInfo(record.getRuleId(), orderQuantity, finalSetId);

                    OrderQuantityResult result = orderQuantityCalcService.calculateOrderQuantities(
                            MatchTypeEnum.KSH, setInfo.getBigDecimal("orderNumber"), matchMethodJson, finalSetId);

                    long c = 0;
                    c += sendOrdersToMQ(baseOrder, pbuAccountList, securities, MatchTypeEnum.KSH,
                            result.getInvalidOrderNumber(), finalSetId, record.getPkId(), "废单", true);
                    c += sendOrdersToMQ(baseOrder, pbuAccountList, securities, MatchTypeEnum.KSH,
                            result.getNoMatchOrderNumber(), finalSetId, record.getPkId(), "不撮合", false);
                    c += sendCancelOrdersToMQ(baseOrder, pbuAccountList, securities, MatchTypeEnum.KSH,
                            result.getCancelOrderNumber(), finalSetId, record.getPkId());
                    c += sendMatchOrdersToMQ(baseOrder, pbuAccountList, securities, MatchTypeEnum.KSH,
                            result.getMatchOrderNumber() / 2, finalSetId, record.getPkId(), MatchOrderQtyEnum.MATCH_ORDER_QTY_ONE);
                    return c;
                } catch (Exception e) {
                    log.error("处理SET {} 时发生错误", finalSetId, e);
                    return 0L;
                }
            }));
        }

        for (Future<Long> f : futures) {
            try { count.addAndGet(f.get()); } catch (Exception e) { log.error("SET任务失败", e); }
        }
        pool.shutdown();
        return count.get();
    }

    public long createAshOrderInfo(SseRuleExecuteRecord record) {
        SseDataRule rule = sseDataRuleService.selectSseDataRuleByPkId(record.getRuleId());
        SseDataRuleInit init = sseDataRuleInitService.selectSseDataRuleInitByRuleId(record.getRuleId());
        String pbuAccountList = init.getPbuAccountList();
        Long orderQuantity = StringUtils.nvl(rule.getOrderQuantity(), 100L) * ORD_QUANTITY_AMPLIFY;
        JSONObject matchMethodJson = JSONObject.parseObject(rule.getMatchMethod());

        JSONObject securitySubCategoryJson = JSON.parseObject(rule.getSecuritySubCategory());
        if (!securitySubCategoryJson.containsKey(SecuritySubCategoryEnum.ASH.getCode())) return 0;
        int ashQuantity = securitySubCategoryJson.getJSONObject(SecuritySubCategoryEnum.ASH.getCode()).getIntValue("quantity");
        BigDecimal ashRatio = securitySubCategoryJson.getJSONObject(SecuritySubCategoryEnum.ASH.getCode()).getBigDecimal("ratio");
        if (ashQuantity == 0) return 0;

        JSONObject ashSecurityJson = StringUtils.isNotBlank(init.getSecurityAshList())
                ? JSON.parseObject(init.getSecurityAshList()) : new JSONObject();
        JSONObject ashSetJson = StringUtils.isNotBlank(init.getSetAshSecurity())
                ? JSON.parseObject(init.getSetAshSecurity()) : new JSONObject();
        JSONObject matchOrderListJson = JSONObject.parseObject(init.getMatchOrderList());

        BigDecimal matching1v1Buyer = matchOrderListJson.getBigDecimal("matching1v1Buyer");
        BigDecimal matching1v10Buyer = matchOrderListJson.getBigDecimal("matching1v10Buyer");
        BigDecimal matching1v2Buyer = matchOrderListJson.getBigDecimal("matching1v2Buyer");

        AtomicLong count = new AtomicLong(0);
        ExecutorService pool = Executors.newFixedThreadPool(Math.min(ashSetJson.size(), 15));
        List<Future<Long>> futures = new ArrayList<>();

        for (String setId : ashSetJson.keySet()) {
            final String finalSetId = setId;
            futures.add(pool.submit(() -> {
                try {
                    JSONObject setInfo = ashSetJson.getJSONObject(finalSetId);
                    JSONArray securities = ashSecurityJson.getJSONArray(finalSetId);
                    if (securities == null || securities.isEmpty()) return 0L;

                    SseOrderInfo baseOrder = defaultOrderInfo(record.getRuleId(), orderQuantity, finalSetId);
                    BigDecimal ratio = setInfo.getBigDecimal("ratio");

                    OrderQuantityResult result = orderQuantityCalcService.calculateOrderQuantities(
                            MatchTypeEnum.ASH, setInfo.getBigDecimal("orderNumber"), matchMethodJson, finalSetId);

                    long c = 0;
                    c += sendOrdersToMQ(baseOrder, pbuAccountList, securities, MatchTypeEnum.ASH,
                            result.getInvalidOrderNumber(), finalSetId, record.getPkId(), "废单", true);
                    c += sendOrdersToMQ(baseOrder, pbuAccountList, securities, MatchTypeEnum.ASH,
                            result.getNoMatchOrderNumber(), finalSetId, record.getPkId(), "不撮合", false);
                    c += sendCancelOrdersToMQ(baseOrder, pbuAccountList, securities, MatchTypeEnum.ASH,
                            result.getCancelOrderNumber(), finalSetId, record.getPkId());

                    int matchCount = matching1v1Buyer.multiply(ratio).divide(ashRatio, 8, RoundingMode.UP).intValue();
                    c += sendMatchOrdersToMQ(baseOrder, pbuAccountList, securities, MatchTypeEnum.ASH,
                            matchCount, finalSetId, record.getPkId(), MatchOrderQtyEnum.MATCH_ORDER_QTY_ONE);

                    int match1v10Count = matching1v10Buyer.multiply(ratio).divide(ashRatio, 8, RoundingMode.UP).intValue();
                    c += sendMatchOrdersToMQ(baseOrder, pbuAccountList, securities, MatchTypeEnum.ASH,
                            match1v10Count, finalSetId, record.getPkId(), MatchOrderQtyEnum.MATCH_ORDER_QTY_TEN);

                    int match1v2Count = matching1v2Buyer.multiply(ratio).divide(ashRatio, 8, RoundingMode.UP).intValue();
                    if (match1v2Count == 0 && matching1v2Buyer.compareTo(BigDecimal.ZERO) > 0) match1v2Count = 1;
                    c += sendMatchOrdersToMQ(baseOrder, pbuAccountList, securities, MatchTypeEnum.ASH,
                            match1v2Count, finalSetId, record.getPkId(), MatchOrderQtyEnum.MATCH_ORDER_QTY_TWO);
                    return c;
                } catch (Exception e) {
                    log.error("处理主板SET {} 时发生错误", finalSetId, e);
                    return 0L;
                }
            }));
        }

        for (Future<Long> f : futures) {
            try { count.addAndGet(f.get()); } catch (Exception e) { log.error("SET任务失败", e); }
        }
        pool.shutdown();
        return count.get();
    }

    private long sendOrdersToMQ(SseOrderInfo baseOrder, String pbuAccountList, JSONArray securities,
                                MatchTypeEnum matchType, int count, String setId, long recordId,
                                String orderType, boolean isInvalid) {
        List<SseOrderInfo> batch = new ArrayList<>(BATCH_SEND_MQ_QUANTITY);
        long generated = 0;
        for (int i = 0; i < count; i++) {
            SseOrderInfo order = isInvalid ? generateInvalidOrder(baseOrder, pbuAccountList, securities, matchType)
                    : generateNoMatchOrder(baseOrder, pbuAccountList, securities, matchType, i);
            batch.add(order);
            generated++;
            if (batch.size() >= BATCH_SEND_MQ_QUANTITY) {
                flushToMQ(batch, setId, orderType, recordId);
                batch.clear();
            }
        }
        if (!batch.isEmpty()) flushToMQ(batch, setId, orderType, recordId);
        return generated;
    }

    private long sendCancelOrdersToMQ(SseOrderInfo baseOrder, String pbuAccountList, JSONArray securities,
                                      MatchTypeEnum matchType, int count, String setId, long recordId) {
        List<SseOrderInfo> batch = new ArrayList<>(BATCH_SEND_MQ_QUANTITY);
        long generated = 0;
        for (int i = 0; i < count / 2; i++) {
            List<SseOrderInfo> pairs = generateCancelOrder(baseOrder, pbuAccountList, securities, matchType);
            batch.addAll(pairs);
            generated += 2;
            if (batch.size() >= BATCH_SEND_MQ_QUANTITY) {
                flushToMQ(batch, setId, "撤单", recordId);
                batch.clear();
            }
        }
        if (!batch.isEmpty()) flushToMQ(batch, setId, "撤单", recordId);
        return generated;
    }

    private long sendMatchOrdersToMQ(SseOrderInfo baseOrder, String pbuAccountList, JSONArray securities,
                                     MatchTypeEnum matchType, int buyerCount, String setId, long recordId,
                                     MatchOrderQtyEnum qtyEnum) {
        List<SseOrderInfo> batch = new ArrayList<>(BATCH_SEND_MQ_QUANTITY);
        long generated = 0;
        for (int i = 0; i < buyerCount; i++) {
            List<SseOrderInfo> matches = generateMatchOrder(baseOrder, pbuAccountList, securities, matchType, qtyEnum);
            batch.addAll(matches);
            generated += matches.size();
            if (batch.size() >= BATCH_SEND_MQ_QUANTITY) {
                flushToMQ(batch, setId, qtyEnum.name(), recordId);
                batch.clear();
            }
        }
        if (!batch.isEmpty()) flushToMQ(batch, setId, qtyEnum.name(), recordId);
        return generated;
    }

    private void flushToMQ(List<SseOrderInfo> orders, String setId, String type, long recordId) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_COMPLETED_EXCHANGE,
                    RabbitMQConfig.MYSQL_INSERT_ROUTING_KEY, JSON.toJSONString(orders));
            log.debug("SET {} 发送{}个{}到MQ", setId, orders.size(), type);

            String cacheKey = "sseRuleExecuteRecord_generatedCount_" + recordId;
            redisCache.increment(cacheKey, orders.size());
            redisCache.expire(cacheKey, 48, TimeUnit.HOURS);
        } catch (Exception e) {
            log.error("发送MQ失败, SET: {}, 类型: {}", setId, type, e);
            throw new RuntimeException(e);
        }
    }

    private Map<String, String> randomPbuAccount(String pbuAccountList) {
        JSONArray array = JSON.parseArray(pbuAccountList);
        JSONObject pbu = array.getJSONObject(new Random().nextInt(array.size()));
        String loginPbu = pbu.getString("loginPbu");
        JSONArray bizArray = pbu.getJSONArray("bizPbuAccount");
        JSONObject bizPbu = bizArray != null && !bizArray.isEmpty()
                ? bizArray.getJSONObject(ThreadLocalRandom.current().nextInt(bizArray.size()))
                : pbu;
        JSONArray accArray = bizPbu.getJSONArray("account");
        Map<String, String> result = new HashMap<>();
        result.put("loginPbu", loginPbu);
        result.put("bizPbu", bizPbu.getString("bizPbu"));
        result.put("account", accArray != null && !accArray.isEmpty()
                ? accArray.getString(new Random().nextInt(accArray.size())) : "");
        return result;
    }

    private SseOrderInfo createBaseOrderInfo(SseOrderInfo base, String pbuAccountList) {
        SseOrderInfo order = new SseOrderInfo();
        BeanUtils.copyProperties(base, order);
        Map<String, String> pa = randomPbuAccount(pbuAccountList);
        order.setBizPbu(pa.get("bizPbu"));
        order.setAccount(pa.get("account"));
        order.setOrderTable("MTP_67_" + pa.get("loginPbu"));
        order.setOrdType("2");
        order.setClOrdId(orderIdGenerateByPbu.generateOrderNo(pa.get("bizPbu")));
        return order;
    }

    private SseSecurityInfo randomSecurityInfo(JSONArray array) {
        JSONObject obj = array.getJSONObject(ThreadLocalRandom.current().nextInt(array.size()));
        SseSecurityInfo info = new SseSecurityInfo();
        info.setSecurityId(obj.getString("securityId"));
        info.setPriorClosingPrice(obj.getDouble("priorClosingPrice"));
        return info;
    }

    private SseOrderInfo generateInvalidOrder(SseOrderInfo base, String pbu, JSONArray sec, MatchTypeEnum type) {
        SseOrderInfo order = createBaseOrderInfo(base, pbu);
        order.setSide("1");
        order.setPrice(BigDecimal.ZERO);
        order.setOrderMatching(type.getInvalidKey());
        order.setSecurityId(randomSecurityInfo(sec).getSecurityId());
        return order;

    }

    private List<SseOrderInfo> generateCancelOrder(SseOrderInfo base, String pbu, JSONArray sec, MatchTypeEnum type) {
        SseOrderInfo original = createBaseOrderInfo(base, pbu);
        SseSecurityInfo si = randomSecurityInfo(sec);
        original.setSecurityId(si.getSecurityId());
        original.setSide(SmallTools.setRamdomOrderSide());
        original.setOrderMatching(type.getCancelKey());
        setOrderPrice(original, BigDecimal.valueOf(si.getPriorClosingPrice()));

        SseOrderInfo cancel = new SseOrderInfo();
        BeanUtils.copyProperties(original, cancel);
        cancel.setMsgHead(MSG_HEAD_CANCEL);
        cancel.setOrigClOrdId(original.getClOrdId());
        cancel.setClOrdId(orderIdGenerateByPbu.generateOrderNo(cancel.getBizPbu()));

        return Arrays.asList(original, cancel);
    }

    private SseOrderInfo generateNoMatchOrder(SseOrderInfo base, String pbu, JSONArray sec, MatchTypeEnum type, int idx) {
        SseOrderInfo order = createBaseOrderInfo(base, pbu);
        SseSecurityInfo si = randomSecurityInfo(sec);
        order.setSecurityId(si.getSecurityId());
        setLimitOrderPrice(order, BigDecimal.valueOf(si.getPriorClosingPrice()), idx);
        order.setOrderMatching(type.getNoMatchKey());
        return order;
    }

    private List<SseOrderInfo> generateMatchOrder(SseOrderInfo base, String pbu, JSONArray sec,
                                                  MatchTypeEnum type, MatchOrderQtyEnum qtyEnum) {
        List<SseOrderInfo> orders = new ArrayList<>();
        SseOrderInfo buyer = createBaseOrderInfo(base, pbu);
        SseSecurityInfo si = randomSecurityInfo(sec);
        BigDecimal price = BigDecimal.valueOf(si.getPriorClosingPrice()).multiply(PRICE_AMPLIFY);
        buyer.setPrice(price);
        buyer.setSecurityId(si.getSecurityId());
        buyer.setSide("1");
        buyer.setOrderMatching(type.getMatchKey());
        if (qtyEnum.getMatchOrderQty() > 1) buyer.setOrderQty(BUYER_QUANTITY * ORD_QUANTITY_AMPLIFY);
        orders.add(buyer);

        for (int i = 0; i < qtyEnum.getMatchOrderQty(); i++) {
            SseOrderInfo seller = createBaseOrderInfo(base, pbu);
            seller.setSide("2");
            seller.setPrice(price);
            seller.setSecurityId(si.getSecurityId());
            seller.setOrderMatching(type.getMatchKey());
            seller.setOrderQty(buyer.getOrderQty() / qtyEnum.getMatchOrderQty());
            orders.add(seller);
        }
        return orders;
    }

    private void setOrderPrice(SseOrderInfo order, BigDecimal closePrice) {
        BigDecimal factor = "1".equals(order.getSide()) ? BigDecimal.valueOf(0.95) : BigDecimal.valueOf(1.05);
        order.setPrice(closePrice.multiply(factor).setScale(2, RoundingMode.HALF_UP).multiply(PRICE_AMPLIFY));
    }

    private void setLimitOrderPrice(SseOrderInfo order, BigDecimal closePrice, int idx) {
        double offset = 0.005 * ((idx % 5) + 1);
        BigDecimal factor = idx % 2 == 0 ? BigDecimal.valueOf(1.0 - offset) : BigDecimal.valueOf(1.0 + offset);
        order.setPrice(closePrice.multiply(factor).setScale(2, RoundingMode.HALF_UP).multiply(PRICE_AMPLIFY));
        order.setSide(idx % 2 == 0 ? "1" : "2");
    }

    private SseOrderInfo defaultOrderInfo(Long ruleId, Long qty, String setId) {
        SseOrderInfo order = new SseOrderInfo();
        order.setRuleId(ruleId);
        order.setMsgHead(MSG_HEAD_CREATE);
        order.setBizId(100010);
        order.setTimeInForce("0");
        order.setBranchId("50001");
        order.setOrderQty(qty);
        order.setOrderSet(setId);
        LocalDateTime time = LocalDateTime.of(TODAY.get(), LocalTime.of(10, 0, 0));
        order.setTransactTime(Date.from(time.atZone(ZoneId.systemDefault()).toInstant()));
        return order;
    }
}

