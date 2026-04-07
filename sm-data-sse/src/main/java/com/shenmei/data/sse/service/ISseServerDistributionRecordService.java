package com.shenmei.data.sse.service;

import com.shenmei.data.common.core.domain.AjaxResult;
import com.shenmei.data.sse.domain.SseServerDistributionRecord;

import java.util.List;

/**
 * 服务器分发记录Service接口
 *
 * @author ruoyi
 * @date 2025-08-05
 */
public interface ISseServerDistributionRecordService
{
    /**
     * 查询服务器分发记录
     *
     * @param pkId 服务器分发记录主键
     * @return 服务器分发记录
     */
    public SseServerDistributionRecord selectSseServerDistributionRecordByPkId(Long pkId);

    /**
     * 查询服务器分发记录列表
     *
     * @param sseServerDistributionRecord 服务器分发记录
     * @return 服务器分发记录集合
     */
    public List<SseServerDistributionRecord> selectSseServerDistributionRecordList(SseServerDistributionRecord sseServerDistributionRecord);

    /**
     * 新增服务器分发记录
     *
     * @param sseServerDistributionRecord 服务器分发记录
     * @return 结果
     */
    public int insertSseServerDistributionRecord(SseServerDistributionRecord sseServerDistributionRecord) throws Exception;

    /**
     * 修改服务器分发记录
     *
     * @param sseServerDistributionRecord 服务器分发记录
     * @return 结果
     */
    public int updateSseServerDistributionRecord(SseServerDistributionRecord sseServerDistributionRecord);

    /**
     * 批量删除服务器分发记录
     *
     * @param pkIds 需要删除的服务器分发记录主键集合
     * @return 结果
     */
    public int deleteSseServerDistributionRecordByPkIds(Long[] pkIds);

    /**
     * 删除服务器分发记录信息
     *
     * @param pkId 服务器分发记录主键
     * @return 结果
     */
    public int deleteSseServerDistributionRecordByPkId(Long pkId);

    /**
     * 获取分发过程记录
     */
    public AjaxResult getProgressInfo(Long pkId);
}

