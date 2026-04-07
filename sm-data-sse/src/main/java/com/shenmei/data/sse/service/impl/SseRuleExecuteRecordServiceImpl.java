package com.shenmei.data.sse.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.shenmei.data.common.core.domain.AjaxResult;
import com.shenmei.data.common.core.redis.RedisCache;
import com.shenmei.data.common.utils.DateUtils;
import com.shenmei.data.common.utils.SecurityUtils;
import com.shenmei.data.common.utils.spring.SpringUtils;
import com.shenmei.data.sse.constant.MatchTypeEnum;
import com.shenmei.data.sse.dataservice.ruledata.CreateOrderInfoService;
import com.shenmei.data.sse.domain.SseRuleExecuteRecord;
import com.shenmei.data.sse.mapper.SseRuleExecuteRecordMapper;
import com.shenmei.data.sse.service.ISseDataRuleService;
import com.shenmei.data.sse.service.ISseRuleExecuteRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shenmei.data.sse.domain.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 规则执行记录Service业务层处理
 *
 * @author ruoyi
 * @date 2025-08-05
 */
@Service
public class SseRuleExecuteRecordServiceImpl implements ISseRuleExecuteRecordService {
    @Autowired
    private SseRuleExecuteRecordMapper sseRuleExecuteRecordMapper;

    @Resource
    private CreateOrderInfoService createOrderInfoService;

    @Resource
    private ISseDataRuleService sseDataRuleService;

    @Autowired
    RedisCache redisCache;
    /**
     * 查询规则执行记录
     *
     * @param pkId 规则执行记录主键
     * @return 规则执行记录
     */
    @Override
    public SseRuleExecuteRecord selectSseRuleExecuteRecordByPkId(Long pkId) {
        return sseRuleExecuteRecordMapper.selectSseRuleExecuteRecordByPkId(pkId);
    }

    /**
     * 查询规则执行记录列表
     *
     * @param sseRuleExecuteRecord 规则执行记录
     * @return 规则执行记录
     */
    @Override
    public List<SseRuleExecuteRecord> selectSseRuleExecuteRecordList(SseRuleExecuteRecord sseRuleExecuteRecord) {
        return sseRuleExecuteRecordMapper.selectSseRuleExecuteRecordList(sseRuleExecuteRecord);
    }

    /**
     * 新增规则执行记录
     *
     * @param sseRuleExecuteRecord 规则执行记录
     * @return 结果
     */
    @Override
    public int insertSseRuleExecuteRecord(SseRuleExecuteRecord sseRuleExecuteRecord) throws Exception {

        SseDataRule sseDataRule = sseDataRuleService.selectSseDataRuleByPkId(sseRuleExecuteRecord.getRuleId());
        String matchMethod = sseDataRule.getMatchMethod();
        JSONObject matchMethodJson = JSONObject.parseObject(matchMethod);
        // 主板废单+科创板废单比例
        BigDecimal ASHInvalidOrderRationOri = new BigDecimal(matchMethodJson.getString(MatchTypeEnum.ASH.getInvalidKey()))
                .divide(new BigDecimal(100), 8, RoundingMode.HALF_UP);
        BigDecimal KSHInvalidOrderRationOri = new BigDecimal(matchMethodJson.getString(MatchTypeEnum.KSH.getInvalidKey()))
                .divide(new BigDecimal(100), 8, RoundingMode.HALF_UP);
        BigDecimal invalidOrderRation = ASHInvalidOrderRationOri.add(KSHInvalidOrderRationOri);

        //主板+科创板撮合订单比例
        BigDecimal ASHMatchOrderRationOri = new BigDecimal(matchMethodJson.getString(MatchTypeEnum.ASH.getMatchKey()))
                .divide(new BigDecimal(100), 8, RoundingMode.HALF_UP);
        BigDecimal KSHMatchOrderRationOri = new BigDecimal(matchMethodJson.getString(MatchTypeEnum.KSH.getMatchKey()))
                .divide(new BigDecimal(100), 8, RoundingMode.HALF_UP);
        BigDecimal matchOrderRation = ASHMatchOrderRationOri.add(KSHMatchOrderRationOri);

        //撮合订单数
        BigDecimal MatchCount = new BigDecimal(sseRuleExecuteRecord.getOrderNumber()).multiply(matchOrderRation);

        //成交订单数 = 订单总数 *（1 - 废单比例）* 成交比
        BigDecimal tradeRatio = sseDataRule.getTradeRatio()
                .divide(new BigDecimal(100), 8, RoundingMode.HALF_UP);
        BigDecimal dealOrderCount = new BigDecimal(sseRuleExecuteRecord.getOrderNumber())
                .multiply(BigDecimal.ONE.subtract(invalidOrderRation))
                .multiply(tradeRatio);
        if (dealOrderCount.compareTo(MatchCount) < 0) {
            throw new Exception("撮合订单数不能大于成交订单数，请检查配置的成交比和撮合占比");
        }

        sseRuleExecuteRecord.setExecuteStatus("2");
        sseRuleExecuteRecord.setExecuteTime(DateUtils.getNowDate());
        sseRuleExecuteRecord.setExecuteUser(SecurityUtils.getUsername());
        int result = sseRuleExecuteRecordMapper.insertSseRuleExecuteRecord(sseRuleExecuteRecord);
        // 异步执行创建订单信息
        ScheduledExecutorService executorService = SpringUtils.getBean("scheduledExecutorService");
        executorService.execute(() -> {
            try {
                createOrderInfoService.createOrderInfo(sseRuleExecuteRecord);
            } catch (Exception e) {
                // 记录异常日志
                e.printStackTrace();
            }
        });
        return result;
    }

    /**
     * 修改规则执行记录
     *
     * @param sseRuleExecuteRecord 规则执行记录
     * @return 结果
     */
    @Override
    public int updateSseRuleExecuteRecord(SseRuleExecuteRecord sseRuleExecuteRecord) {
        return sseRuleExecuteRecordMapper.updateSseRuleExecuteRecord(sseRuleExecuteRecord);
    }

    /**
     * 批量删除规则执行记录
     *
     * @param pkIds 需要删除的规则执行记录主键
     * @return 结果
     */
    @Override
    public int deleteSseRuleExecuteRecordByPkIds(Long[] pkIds) {
        return sseRuleExecuteRecordMapper.deleteSseRuleExecuteRecordByPkIds(pkIds);
    }

    /**
     * 删除规则执行记录信息
     *
     * @param pkId 规则执行记录主键
     * @return 结果
     */
    @Override
    public int deleteSseRuleExecuteRecordByPkId(Long pkId) {
        return sseRuleExecuteRecordMapper.deleteSseRuleExecuteRecordByPkId(pkId);
    }


    /**
     * 获取规则执行过程记录
     */
    @Override
    public AjaxResult getProgressInfo(Long pkId) {
        SseRuleExecuteRecord record = selectSseRuleExecuteRecordByPkId(pkId);
        Map<String, Object> map = new HashMap<>();
        map.put("totalCount", record.getOrderNumber());
        map.put("orderType", record.getOrderType());
        //如果record状态是完成，则直接返回
        if ("0".equals(record.getExecuteStatus())) {
            map.put("generatedCount", record.getOrderNumber());
        }else {
            String cacheKey = "sseRuleExecuteRecord_generatedCount_" + record.getPkId();
            // 从Redis获取当前生成数量
            Object cachedValue = redisCache.getCacheObject(cacheKey);
            long generatedCount = (cachedValue != null) ? Long.parseLong(cachedValue.toString()) : 0;
            map.put("generatedCount", generatedCount);
        }
        return AjaxResult.success(map);

    }


    /**
     * 根据规则 ID 获取订单总数（全量 + 增量累加）
     */
    @Override
    public AjaxResult getTotalOrderCountByRuleId(Long ruleId) {
        try {
            SseRuleExecuteRecord queryParam = new SseRuleExecuteRecord();
            queryParam.setRuleId(ruleId);

            // 查询该规则的所有执行记录
            List<SseRuleExecuteRecord> records = sseRuleExecuteRecordMapper.selectSseRuleExecuteRecordList(queryParam);

            if (records == null || records.isEmpty()) {
                return AjaxResult.success(0L);
            }

            // 按 pkId 倒序排列
            records.sort((a, b) -> b.getPkId().compareTo(a.getPkId()));

            long totalCount = 0;
            SseRuleExecuteRecord firstRecord = records.get(0);

            if ("0".equals(firstRecord.getOrderType())) {
                // 第一条是全量，只取这一条的订单数
                totalCount = firstRecord.getOrderNumber() != null ? firstRecord.getOrderNumber() : 0;
            } else {
                // 第一条是增量，需要累加直到遇到全量记录
                for (SseRuleExecuteRecord record : records) {
                    totalCount += record.getOrderNumber() != null ? record.getOrderNumber() : 0;
                    if ("0".equals(record.getOrderType())) {
                        // 遇到全量记录，停止累加
                        break;
                    }
                }
            }

            return AjaxResult.success(totalCount);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("获取订单总数失败：" + e.getMessage());
        }
    }
}
