package com.shenmei.data.sse.service.impl;

import com.shenmei.data.sse.domain.SseOrderInfo;
import com.shenmei.data.sse.mapper.SseOrderInfoMapper;
import com.shenmei.data.sse.service.ISseOrderInfoService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 订单汇总Service业务层处理
 *
 * @author song
 * @date 2025-08-02
 */
@Service
public class SseOrderInfoServiceImpl implements ISseOrderInfoService
{
    @Autowired
    private SseOrderInfoMapper sseOrderInfoMapper;

    /**
     * 查询订单汇总
     *
     * @param pkId 订单汇总主键
     * @return 订单汇总
     */
    @Override
    public SseOrderInfo selectSseOrderInfoByPkId(Long pkId)
    {
        return sseOrderInfoMapper.selectSseOrderInfoByPkId(pkId);
    }

    /**
     * 查询订单汇总列表
     *
     * @param sseOrderInfo 订单汇总
     * @return 订单汇总
     */
    @Override
    public List<SseOrderInfo> selectSseOrderInfoList(SseOrderInfo sseOrderInfo)
    {
        return sseOrderInfoMapper.selectSseOrderInfoList(sseOrderInfo);
    }

    /**
     * 新增订单汇总
     *
     * @param sseOrderInfo 订单汇总
     * @return 结果
     */
    @Override
    public int insertSseOrderInfo(SseOrderInfo sseOrderInfo)
    {
        return sseOrderInfoMapper.insertSseOrderInfo(sseOrderInfo);
    }

    /**
     * 修改订单汇总
     *
     * @param sseOrderInfo 订单汇总
     * @return 结果
     */
    @Override
    public int updateSseOrderInfo(SseOrderInfo sseOrderInfo)
    {
        return sseOrderInfoMapper.updateSseOrderInfo(sseOrderInfo);
    }

    /**
     * 批量删除订单汇总
     *
     * @param pkIds 需要删除的订单汇总主键
     * @return 结果
     */
    @Override
    public int deleteSseOrderInfoByPkIds(Long[] pkIds)
    {
        return sseOrderInfoMapper.deleteSseOrderInfoByPkIds(pkIds);
    }

    /**
     * 删除订单汇总信息
     *
     * @param pkId 订单汇总主键
     * @return 结果
     */
    @Override
    public int deleteSseOrderInfoByPkId(Long pkId)
    {
        return sseOrderInfoMapper.deleteSseOrderInfoByPkId(pkId);
    }

    @Override
    public int deleteSseOrderInfoByRuleId(Long ruleId) {return sseOrderInfoMapper.deleteSseOrderInfoByRuleId(ruleId);}

    @Override
    public int batchInsertSseOrderInfo(List<SseOrderInfo> orderInfoList) {
        return sseOrderInfoMapper.batchInsertSseOrderInfo(orderInfoList);
    }

    @Override
    public List<String> selectOrderTableListByRuleId(long ruleId) {
        return sseOrderInfoMapper.selectOrderTableListByRuleId(ruleId);
    }

    @Override
    public long countTotalOrderNumberByRuleId(Long ruleId) {
        return sseOrderInfoMapper.countTotalOrderNumberByRuleId(ruleId);
    }

    @Override
    public long countOrderNumbersBySseOrderInfo(SseOrderInfo sseOrderInfo) {
        return sseOrderInfoMapper.countOrderNumbersBySseOrderInfo(sseOrderInfo);
    }

    @Override
    public List<String> selectStatsListByRuleId(
            @Param("category") String category,
            @Param("ruleId") Long ruleId) {

        // 校验字段是否在允许的范围内
        List<String> allowedFields = Arrays.asList("biz_pbu", "order_set", "order_matching","account","ord_type","security_id");
        if (!allowedFields.contains(category)) {
            throw new IllegalArgumentException("不支持的字段名：" + category);
        }
        return sseOrderInfoMapper.selectStatsListByRuleId(category, ruleId);
    }

    @Override
    public List<SseOrderInfo> selectSseOrderInfoListByPage(SseOrderInfo sseOrderInfo, int offset, int limit) {
        return sseOrderInfoMapper.selectSseOrderInfoListByPage(sseOrderInfo, offset, limit);
    }

    @Override
    public List<String> selectAccountBySecurityId(String securityId,Long ruleId){
        return sseOrderInfoMapper.selectAccountBySecurityId(securityId,ruleId);
    }
}
