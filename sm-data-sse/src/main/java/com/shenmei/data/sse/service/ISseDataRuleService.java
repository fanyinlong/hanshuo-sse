package com.shenmei.data.sse.service;

import java.util.List;
import com.shenmei.data.sse.domain.SseDataRule;

/**
 * 规则配置Service接口
 * 
 * @author teabot
 * @date 2025-07-19
 */
public interface ISseDataRuleService 
{
    /**
     * 查询规则配置
     * 
     * @param pkId 规则配置主键
     * @return 规则配置
     */
    public SseDataRule selectSseDataRuleByPkId(Long pkId);

    /**
     * 查询规则配置列表
     * 
     * @param sseDataRule 规则配置
     * @return 规则配置集合
     */
    public List<SseDataRule> selectSseDataRuleList(SseDataRule sseDataRule);

    /**
     * 新增规则配置
     * 
     * @param sseDataRule 规则配置
     * @return 结果
     */
    public int insertSseDataRule(SseDataRule sseDataRule);

    /**
     * 修改规则配置
     * 
     * @param sseDataRule 规则配置
     * @return 结果
     */
    public int updateSseDataRule(SseDataRule sseDataRule);

    /**
     * 批量删除规则配置
     * 
     * @param pkIds 需要删除的规则配置主键集合
     * @return 结果
     */
    public int deleteSseDataRuleByPkIds(Long[] pkIds);

    /**
     * 删除规则配置信息
     * 
     * @param pkId 规则配置主键
     * @return 结果
     */
    public int deleteSseDataRuleByPkId(Long pkId);
}
