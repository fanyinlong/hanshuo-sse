package com.shenmei.data.sse.mapper;

import com.shenmei.data.sse.domain.SseOrderInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单汇总Mapper接口
 *
 * @author song
 * @date 2025-08-02
 */
public interface SseOrderInfoMapper
{
    /**
     * 查询订单汇总
     *
     * @param pkId 订单汇总主键
     * @return 订单汇总
     */
    public SseOrderInfo selectSseOrderInfoByPkId(Long pkId);

    /**
     * 查询订单汇总列表
     *
     * @param sseOrderInfo 订单汇总
     * @return 订单汇总集合
     */
    public List<SseOrderInfo> selectSseOrderInfoList(SseOrderInfo sseOrderInfo);

    /**
     * 新增订单汇总
     *
     * @param sseOrderInfo 订单汇总
     * @return 结果
     */
    public int insertSseOrderInfo(SseOrderInfo sseOrderInfo);

    /**
     * 修改订单汇总
     *
     * @param sseOrderInfo 订单汇总
     * @return 结果
     */
    public int updateSseOrderInfo(SseOrderInfo sseOrderInfo);

    /**
     * 删除订单汇总
     *
     * @param pkId 订单汇总主键
     * @return 结果
     */
    public int deleteSseOrderInfoByPkId(Long pkId);

    /**
     * 删除订单汇总
     *
     * @param ruleId 订单汇总
     * @return 结果
     */
    public int deleteSseOrderInfoByRuleId(Long ruleId);

    /**
     * 批量删除订单汇总
     *
     * @param pkIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSseOrderInfoByPkIds(Long[] pkIds);


    /**
     * 批量插入订单信息
     *
     * @param orderInfoList 订单信息列表
     * @return 插入记录数
     */
    int batchInsertSseOrderInfo(List<SseOrderInfo> orderInfoList);

    /**
     * @param ruleId
     * @return sseOrderInfo
     */

    List<String> selectOrderTableListByRuleId(Long ruleId);

    long countTotalOrderNumberByRuleId(Long ruleId);

    long countOrderNumbersBySseOrderInfo(SseOrderInfo sseOrderInfo);

    List<String> selectStatsListByRuleId(@Param("category") String category, @Param("ruleId") Long ruleId);

    List<SseOrderInfo> selectSseOrderInfoListByPage(
            @Param("query") SseOrderInfo query,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    List<String> selectAccountBySecurityId(@Param("securityId") String securityId, @Param("ruleId") Long ruleId);
}
