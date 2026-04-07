package com.shenmei.data.sse.service.impl;

import com.shenmei.data.common.utils.DateUtils;
import com.shenmei.data.common.utils.SecurityUtils;
import com.shenmei.data.sse.dataservice.ruledata.RuleValidationService;
import com.shenmei.data.sse.domain.SseDataRule;
import com.shenmei.data.sse.mapper.SseDataRuleMapper;
import com.shenmei.data.sse.service.ISseDataRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 规则配置Service业务层处理
 * 
 * @author teabot
 * @date 2025-07-19
 */
@Service
public class SseDataRuleServiceImpl implements ISseDataRuleService 
{
    @Autowired
    private SseDataRuleMapper sseDataRuleMapper;

    @Autowired
    private RuleValidationService ruleValidationService;

    /**
     * 查询规则配置
     * 
     * @param pkId 规则配置主键
     * @return 规则配置
     */
    @Override
    public SseDataRule selectSseDataRuleByPkId(Long pkId)
    {
        return sseDataRuleMapper.selectSseDataRuleByPkId(pkId);
    }

    /**
     * 查询规则配置列表
     * 
     * @param sseDataRule 规则配置
     * @return 规则配置
     */
    @Override
    public List<SseDataRule> selectSseDataRuleList(SseDataRule sseDataRule)
    {
        return sseDataRuleMapper.selectSseDataRuleList(sseDataRule);
    }

    /**
     * 新增规则配置
     * 
     * @param sseDataRule 规则配置
     *
     * @return 结果
     */
    @Override
    public int insertSseDataRule(SseDataRule sseDataRule)
    {
        // 验证规则配置并计算每个 SET 的股票和订单分配
        Map<String, Object> validationResult = ruleValidationService.validateAndCalculateSetAllocation(sseDataRule);

        // 如果验证失败，抛出异常
        if (!(Boolean) validationResult.get("success")) {
            String errors = (String) validationResult.get("errors");
            throw new RuntimeException(errors);
        }
        sseDataRule.setCreateTime(DateUtils.getNowDate());
        sseDataRule.setCreateBy(SecurityUtils.getUsername());
        return sseDataRuleMapper.insertSseDataRule(sseDataRule);
    }

    /**
     * 修改规则配置
     * 
     * @param sseDataRule 规则配置
     * @return 结果
     */
    @Override
    public int updateSseDataRule(SseDataRule sseDataRule)
    {
        sseDataRule.setUpdateTime(DateUtils.getNowDate());
        sseDataRule.setUpdateBy(SecurityUtils.getUsername());
        return sseDataRuleMapper.updateSseDataRule(sseDataRule);
    }

    /**
     * 批量删除规则配置
     * 
     * @param pkIds 需要删除的规则配置主键
     * @return 结果
     */
    @Override
    public int deleteSseDataRuleByPkIds(Long[] pkIds)
    {
        return sseDataRuleMapper.deleteSseDataRuleByPkIds(pkIds);
    }

    /**
     * 删除规则配置信息
     * 
     * @param pkId 规则配置主键
     * @return 结果
     */
    @Override
    public int deleteSseDataRuleByPkId(Long pkId)
    {
        return sseDataRuleMapper.deleteSseDataRuleByPkId(pkId);
    }
}
