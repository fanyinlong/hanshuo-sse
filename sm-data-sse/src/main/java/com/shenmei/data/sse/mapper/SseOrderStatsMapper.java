package com.shenmei.data.sse.mapper;

import java.util.List;
import com.shenmei.data.sse.domain.SseOrderStats;
import org.apache.ibatis.annotations.Param;

/**
 * 订单统计Mapper接口
 *
 * @author Song
 * @date 2025-08-11
 */
public interface SseOrderStatsMapper
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
     * 删除订单统计
     *
     * @param pkId 订单统计主键
     * @return 结果
     */
    public int deleteSseOrderStatsByPkId(Long pkId);

    /**
     * 批量删除订单统计
     *
     * @param pkIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSseOrderStatsByPkIds(Long[] pkIds);

    List<String> selectOrdersStatsDetailByRuleId(@Param("category") String category, @Param("ruleId") Long ruleId);

}
