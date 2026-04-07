package com.shenmei.data.sse.service.impl;

import com.shenmei.data.common.core.domain.AjaxResult;
import com.shenmei.data.common.core.redis.RedisCache;
import com.shenmei.data.common.utils.DateUtils;
import com.shenmei.data.common.utils.SecurityUtils;
import com.shenmei.data.common.utils.spring.SpringUtils;
import com.shenmei.data.sse.domain.SseServerDistributionRecord;
import com.shenmei.data.sse.mapper.SseServerDistributionRecordMapper;
import com.shenmei.data.sse.service.ISseServerDistributionRecordService;
import com.shenmei.data.sse.service.ISseServerRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 服务器分发记录Service业务层处理
 *
 * @author ruoyi
 * @date 2025-08-05
 */
@Service
public class SseServerDistributionRecordServiceImpl implements ISseServerDistributionRecordService {
    @Autowired
    private SseServerDistributionRecordMapper sseServerDistributionRecordMapper;

    @Autowired
    private ISseServerRuleService sseServerRuleService;

    @Autowired
    RedisCache redisCache;

    /**
     * 查询服务器分发记录
     *
     * @param pkId 服务器分发记录主键
     * @return 服务器分发记录
     */
    @Override
    public SseServerDistributionRecord selectSseServerDistributionRecordByPkId(Long pkId) {
        return sseServerDistributionRecordMapper.selectSseServerDistributionRecordByPkId(pkId);
    }

    /**
     * 查询服务器分发记录列表
     *
     * @param sseServerDistributionRecord 服务器分发记录
     * @return 服务器分发记录
     */
    @Override
    public List<SseServerDistributionRecord> selectSseServerDistributionRecordList(SseServerDistributionRecord sseServerDistributionRecord) {
        return sseServerDistributionRecordMapper.selectSseServerDistributionRecordList(sseServerDistributionRecord);
    }

    /**
     * 新增服务器分发记录
     *
     * @param sseServerDistributionRecord 服务器分发记录
     * @return 结果
     */
    @Override
    public int insertSseServerDistributionRecord(SseServerDistributionRecord sseServerDistributionRecord) throws Exception {
        // 检查是否已存在正在执行或已完成的记录
        SseServerDistributionRecord queryRecord = new SseServerDistributionRecord();
        queryRecord.setRuleId(sseServerDistributionRecord.getRuleId());
        queryRecord.setDistributionStatus("2"); // 执行中
        List<SseServerDistributionRecord> existingRecords = sseServerDistributionRecordMapper.selectSseServerDistributionRecordList(queryRecord);

// 也可以查询已完成的记录
        queryRecord.setDistributionStatus("0"); // 已完成
        existingRecords.addAll(sseServerDistributionRecordMapper.selectSseServerDistributionRecordList(queryRecord));

        if (!existingRecords.isEmpty()) {
            throw new Exception("该规则的数据分发任务已在执行中或已完成，请勿重复提交");
        }

        sseServerDistributionRecord.setDistributionStatus("2"); // 2表示执行中
        sseServerDistributionRecord.setDistributionTime(DateUtils.getNowDate());
        sseServerDistributionRecord.setDistributionUser(SecurityUtils.getUsername());
        int result = sseServerDistributionRecordMapper.insertSseServerDistributionRecord(sseServerDistributionRecord);

        // 异步执行分发任务
        ScheduledExecutorService executorService = SpringUtils.getBean("scheduledExecutorService");
        executorService.execute(() -> {
            try {
                sseServerRuleService.orderDataDistribution(sseServerDistributionRecord);

                // 更新分发状态为完成，并记录总条数
                sseServerDistributionRecord.setDistributionStatus("0"); // 0 表示完成
                // 从 Redis 获取总条数
                String cacheKey = "sseServerDistribution_generatedCount_" + sseServerDistributionRecord.getPkId();
                Object cachedValue = redisCache.getCacheObject(cacheKey);
                if (cachedValue != null) {
                    sseServerDistributionRecord.setTotalCount(Long.parseLong(cachedValue.toString()));
                }
                sseServerDistributionRecordMapper.updateSseServerDistributionRecord(sseServerDistributionRecord);

                // 清除 Redis 缓存
                redisCache.deleteObject(cacheKey);
            } catch (Exception e) {
                // 记录异常日志并更新状态为失败
                e.printStackTrace();
                sseServerDistributionRecord.setDistributionStatus("1"); // 1 表示失败
                // 记录错误信息
                String errorMsg = e.getMessage() != null ? e.getMessage() : "未知错误";
                sseServerDistributionRecord.setErrorMessage(errorMsg);
                sseServerDistributionRecordMapper.updateSseServerDistributionRecord(sseServerDistributionRecord);
            }
        });

        return result;
    }

    /**
     * 修改服务器分发记录
     *
     * @param sseServerDistributionRecord 服务器分发记录
     * @return 结果
     */
    @Override
    public int updateSseServerDistributionRecord(SseServerDistributionRecord sseServerDistributionRecord) {
        return sseServerDistributionRecordMapper.updateSseServerDistributionRecord(sseServerDistributionRecord);
    }

    /**
     * 批量删除服务器分发记录
     *
     * @param pkIds 需要删除的服务器分发记录主键
     * @return 结果
     */
    @Override
    public int deleteSseServerDistributionRecordByPkIds(Long[] pkIds) {
        return sseServerDistributionRecordMapper.deleteSseServerDistributionRecordByPkIds(pkIds);
    }

    /**
     * 删除服务器分发记录信息
     *
     * @param pkId 服务器分发记录主键
     * @return 结果
     */
    @Override
    public int deleteSseServerDistributionRecordByPkId(Long pkId) {
        return sseServerDistributionRecordMapper.deleteSseServerDistributionRecordByPkId(pkId);
    }

    /**
     * 获取分发过程记录
     */
    @Override
    public AjaxResult getProgressInfo(Long pkId) {
        SseServerDistributionRecord record = selectSseServerDistributionRecordByPkId(pkId);
        Map<String, Object> map = new HashMap<>();

        // 如果record状态是完成，则直接返回
        if ("0".equals(record.getDistributionStatus())) {
            map.put("status", "completed");
            map.put("message", "分发已完成");
        } else if ("1".equals(record.getDistributionStatus())) {
            map.put("status", "failed");
            map.put("message", "分发失败");
        } else {
            map.put("status", "running");
            map.put("message", "分发进行中");

            // 从Redis获取当前分发进度
            String cacheKey = "sseServerDistribution_generatedCount_" + record.getPkId();
            Object cachedValue = redisCache.getCacheObject(cacheKey);
            long generatedCount = (cachedValue != null) ? Long.parseLong(cachedValue.toString()) : 0;
            map.put("generatedCount", generatedCount);
        }

        return AjaxResult.success(map);
    }
}

