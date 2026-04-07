package com.shenmei.data.sse.mapper;

import com.shenmei.data.sse.domain.SseDataRuleInit;

import java.util.List;


/**
 * 规则初始化Mapper接口
 * 
 * @author teabot
 * @date 2025-08-02
 */
public interface SseDataRuleInitMapper 
{
    /**
     * 查询规则初始化
     * 
     * @param pkId 规则初始化主键
     * @return 规则初始化
     */
    public SseDataRuleInit selectSseDataRuleInitByPkId(Long pkId);

    /**
     * 查询规则初始化列表
     * 
     * @param sseDataRuleInit 规则初始化
     * @return 规则初始化集合
     */
    public List<SseDataRuleInit> selectSseDataRuleInitList(SseDataRuleInit sseDataRuleInit);

    /**
     * 新增规则初始化
     * 
     * @param sseDataRuleInit 规则初始化
     * @return 结果
     */
    public int insertSseDataRuleInit(SseDataRuleInit sseDataRuleInit);

    /**
     * 修改规则初始化
     * 
     * @param sseDataRuleInit 规则初始化
     * @return 结果
     */
    public int updateSseDataRuleInit(SseDataRuleInit sseDataRuleInit);

    /**
     * 删除规则初始化
     * 
     * @param pkId 规则初始化主键
     * @return 结果
     */
    public int deleteSseDataRuleInitByPkId(Long pkId);

    /**
     * 批量删除规则初始化
     * 
     * @param pkIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSseDataRuleInitByPkIds(Long[] pkIds);

    /**
     * 查询规则初始化
     *
     * @param ruleId 规则iD
     * @return 规则初始化
     */
    public SseDataRuleInit selectSseDataRuleInitByRuleId(Long ruleId);

}
