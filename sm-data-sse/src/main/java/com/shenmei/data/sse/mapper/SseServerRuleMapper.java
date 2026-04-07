package com.shenmei.data.sse.mapper;

import com.shenmei.data.sse.domain.SseServerConfig;
import com.shenmei.data.sse.domain.SseServerInfo;
import com.shenmei.data.sse.domain.SseServerRule;

import java.util.List;

public interface SseServerRuleMapper {
    /**
     * 新增规则信息
     *
     * @param sseServiceRule 主机规则信息
     * @return 结果
     */
    public int insertSseServerRule(SseServerRule sseServiceRule);

    /**
     * 查询规则信息
     *
     * @param sseServiceRule 主机信息主键
     * @return 主机信息
     */
    public SseServerRule selectSseServerRuleByServerIdAndRuleid(SseServerRule sseServiceRule);

    /**
     * 查询主机规则信息列表
     *
     * @param sseServiceRule 主机规则信息
     * @return 主机规则信息集合
     */
    public List<SseServerRule> selectSseServerRuleList(SseServerRule sseServiceRule);

    /**
     * 批量删除主机规则信息
     *
     * @param serverIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSseServerRuleByServerIds(String[] ids);

    public SseServerRule selectSseServerRuleById(String id);

    public int updateSseServerRule(SseServerRule sseServiceRule);

    public List<SseServerRule> selectSseServerConfigByRuleId(Long RuleId);

}
