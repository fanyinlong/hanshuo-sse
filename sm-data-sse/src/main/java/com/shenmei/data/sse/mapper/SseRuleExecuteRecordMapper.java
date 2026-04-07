package com.shenmei.data.sse.mapper;

import com.shenmei.data.sse.domain.SseRuleExecuteRecord;

import java.util.List;

/**
 * 规则执行记录Mapper接口
 * 
 * @author ruoyi
 * @date 2025-08-05
 */
public interface SseRuleExecuteRecordMapper 
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
    public int insertSseRuleExecuteRecord(SseRuleExecuteRecord sseRuleExecuteRecord);

    /**
     * 修改规则执行记录
     * 
     * @param sseRuleExecuteRecord 规则执行记录
     * @return 结果
     */
    public int updateSseRuleExecuteRecord(SseRuleExecuteRecord sseRuleExecuteRecord);

    /**
     * 删除规则执行记录
     * 
     * @param pkId 规则执行记录主键
     * @return 结果
     */
    public int deleteSseRuleExecuteRecordByPkId(Long pkId);

    /**
     * 批量删除规则执行记录
     * 
     * @param pkIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSseRuleExecuteRecordByPkIds(Long[] pkIds);
}
