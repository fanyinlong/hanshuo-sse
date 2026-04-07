package com.shenmei.data.sse.service;

import com.shenmei.data.common.core.domain.AjaxResult;
import com.shenmei.data.sse.domain.SseRuleExecuteRecord;

import java.util.List;

/**
 * 规则执行记录Service接口
 * 
 * @author ruoyi
 * @date 2025-08-05
 */
public interface ISseRuleExecuteRecordService 
{
    /**
     * 查询规则执行记录
     * 
     * @param pkId 规则执行记录主键
     * @return 规则执行记录
     */
    public SseRuleExecuteRecord selectSseRuleExecuteRecordByPkId(Long pkId);

    /**
     * 查询规则执行记录列表
     * 
     * @param sseRuleExecuteRecord 规则执行记录
     * @return 规则执行记录集合
     */
    public List<SseRuleExecuteRecord> selectSseRuleExecuteRecordList(SseRuleExecuteRecord sseRuleExecuteRecord);

    /**
     * 新增规则执行记录
     * 
     * @param sseRuleExecuteRecord 规则执行记录
     * @return 结果
     */
    public int insertSseRuleExecuteRecord(SseRuleExecuteRecord sseRuleExecuteRecord) throws Exception;

    /**
     * 修改规则执行记录
     * 
     * @param sseRuleExecuteRecord 规则执行记录
     * @return 结果
     */
    public int updateSseRuleExecuteRecord(SseRuleExecuteRecord sseRuleExecuteRecord);

    /**
     * 批量删除规则执行记录
     * 
     * @param pkIds 需要删除的规则执行记录主键集合
     * @return 结果
     */
    public int deleteSseRuleExecuteRecordByPkIds(Long[] pkIds);

    /**
     * 删除规则执行记录信息
     * 
     * @param pkId 规则执行记录主键
     * @return 结果
     */
    public int deleteSseRuleExecuteRecordByPkId(Long pkId);


    /**
     * 获取规则执行过程记录
     */
    public AjaxResult getProgressInfo(Long pkId);

    /**
     * 根据规则 ID 获取订单总数（全量 + 增量累加）
     *
     * @param ruleId 规则 ID
     * @return 订单总数
     */
    public AjaxResult getTotalOrderCountByRuleId(Long ruleId);
}
