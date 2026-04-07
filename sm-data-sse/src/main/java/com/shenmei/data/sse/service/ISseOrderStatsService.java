package com.shenmei.data.sse.service;

import java.util.List;
import com.shenmei.data.sse.domain.SseOrderStats;

/**
 * 订单统计Service接口
 *
 * @author Song
 * @date 2025-08-11
 */
public interface ISseOrderStatsService
{
    /**
     * 查询订单统计
     *
     * @param pkId 订单统计主键
     * @return 订单统计
     */
    public SseOrderStats selectSseOrderStatsByPkId(Long pkId);

    /**
     * 查询订单统计列表
     *
     * @param sseOrderStats 订单统计
     * @return 订单统计集合
     */
    public List<SseOrderStats> selectSseOrderStatsList(SseOrderStats sseOrderStats);

    /**
     * 新增订单统计
     *
     * @param sseOrderStats 订单统计
     * @return 结果
     */
    public int insertSseOrderStats(SseOrderStats sseOrderStats);

    /**
     * 修改订单统计
     *
     * @param sseOrderStats 订单统计
     * @return 结果
     */
    public int updateSseOrderStats(SseOrderStats sseOrderStats);

    /**
     * 批量删除订单统计
     *
     * @param pkIds 需要删除的订单统计主键集合
     * @return 结果
     */
    public int deleteSseOrderStatsByPkIds(Long[] pkIds);

    /**
     * 删除订单统计信息
     *
     * @param pkId 订单统计主键
     * @return 结果
     */
    public int deleteSseOrderStatsByPkId(Long pkId);

    public List<String> selectOrdersStatsDetailByRuleId(String category,Long ruleId);
}
