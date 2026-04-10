package com.shenmei.data.sse.service;

import com.shenmei.data.sse.domain.SseOrderInfo;
import com.shenmei.data.sse.domain.SseServerDistributionRecord;
import com.shenmei.data.sse.domain.SseServerInfo;
import com.shenmei.data.sse.domain.SseServerRule;

import java.util.List;

public interface ISseServerRuleService {

    /**
     * 新增主机规则信息
     *
     * @param sseServerRule 主机信息
     * @return 结果
     */
    public int insertSseServerRule(SseServerRule sseServerRule) throws Exception;

    /**
     * 查询主机规则列表
     *
     * @param sseServerRule 主机规则信息
     * @return 主机规则信息集合
     */
    public List<SseServerRule> selectSseServerInfoList(SseServerRule sseServerRule) throws Exception;

    public int deleteSseServerRuleByServerIds(String[] ids);

    public SseServerRule selectSseServerRuleById(String id);


    /**
     * 修改主机规则
     *
     * @param sseServerRule 主机规则
     * @return 结果
     */
    public int updateSseServerRule(SseServerRule sseServerRule) throws Exception;


    public  void orderDataDistribution(SseServerDistributionRecord sseServerDistributionRecord);

    void orderDataDistributionWithOrders(SseServerDistributionRecord record, List<SseOrderInfo> orders);

}
