package com.shenmei.data.sse.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shenmei.data.sse.mapper.SseOrderStatsMapper;
import com.shenmei.data.sse.domain.SseOrderStats;
import com.shenmei.data.sse.service.ISseOrderStatsService;

/**
 * 订单统计Service业务层处理
 *
 * @author Song
 * @date 2025-08-11
 */
@Service
public class SseOrderStatsServiceImpl implements ISseOrderStatsService
{
    @Autowired
    private SseOrderStatsMapper sseOrderStatsMapper;

    /**
     * 查询订单统计
     *
     * @param pkId 订单统计主键
     * @return 订单统计
     */
    @Override
    public SseOrderStats selectSseOrderStatsByPkId(Long pkId)
    {
        return sseOrderStatsMapper.selectSseOrderStatsByPkId(pkId);
    }

    /**
     * 查询订单统计列表
     *
     * @param sseOrderStats 订单统计
     * @return 订单统计
     */
    @Override
    public List<SseOrderStats> selectSseOrderStatsList(SseOrderStats sseOrderStats)
    {
        return sseOrderStatsMapper.selectSseOrderStatsList(sseOrderStats);
    }

    /**
     * 新增订单统计
     *
     * @param sseOrderStats 订单统计
     * @return 结果
     */
    @Override
    public int insertSseOrderStats(SseOrderStats sseOrderStats)
    {
        return sseOrderStatsMapper.insertSseOrderStats(sseOrderStats);
    }

    /**
     * 修改订单统计
     *
     * @param sseOrderStats 订单统计
     * @return 结果
     */
    @Override
    public int updateSseOrderStats(SseOrderStats sseOrderStats)
    {
        return sseOrderStatsMapper.updateSseOrderStats(sseOrderStats);
    }

    /**
     * 批量删除订单统计
     *
     * @param pkIds 需要删除的订单统计主键
     * @return 结果
     */
    @Override
    public int deleteSseOrderStatsByPkIds(Long[] pkIds)
    {
        return sseOrderStatsMapper.deleteSseOrderStatsByPkIds(pkIds);
    }

    /**
     * 删除订单统计信息
     *
     * @param pkId 订单统计主键
     * @return 结果
     */
    @Override
    public int deleteSseOrderStatsByPkId(Long pkId)
    {
        return sseOrderStatsMapper.deleteSseOrderStatsByPkId(pkId);
    }

    @Override
    public List<String> selectOrdersStatsDetailByRuleId(String category,Long ruleId){
        return sseOrderStatsMapper.selectOrdersStatsDetailByRuleId(category, ruleId);
    }


}
