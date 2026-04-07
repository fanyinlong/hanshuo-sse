package com.shenmei.data.sse.service;

import java.util.List;
import com.shenmei.data.sse.domain.SseAccountInfo;
import com.shenmei.data.sse.domain.SseAccountInfo;

/**
 * 账户信息Service接口
 *
 * @author song
 * @date 2025-07-28
 */
public interface ISseAccountInfoService
{
    /**
     * 查询账户信息
     *
     * @param pkId 账户信息主键
     * @return 账户信息
     */
    public SseAccountInfo selectSseAccountInfoByPkId(Long pkId);

    /**
     * 查询账户信息列表
     *
     * @param sseAccountInfo 账户信息
     * @return 账户信息集合
     */
    public List<SseAccountInfo> selectSseAccountInfoList(SseAccountInfo sseAccountInfo);

    /**
     * 新增账户信息
     *
     * @param sseAccountInfo 账户信息
     * @return 结果
     */
    public int insertSseAccountInfo(SseAccountInfo sseAccountInfo);

    /**
     * 修改账户信息
     *
     * @param sseAccountInfo 账户信息
     * @return 结果
     */
    public int updateSseAccountInfo(SseAccountInfo sseAccountInfo);

    /**
     * 批量删除账户信息
     *
     * @param pkIds 需要删除的账户信息主键集合
     * @return 结果
     */
    public int deleteSseAccountInfoByPkIds(Long[] pkIds);

    /**
     * 删除账户信息信息
     *
     * @param pkId 账户信息主键
     * @return 结果
     */
    public int deleteSseAccountInfoByPkId(Long pkId);

    /**
     * 批量插入Account信息
     * @param sseAccountInfoList 产品信息列表
     * @return 插入记录数
     */
    int batchInsertSseAccountInfo(List<SseAccountInfo> sseAccountInfoList);

    /**
     * 初始化Account信息
     */
    void init();
    /**
     * 清空Account信息
     */
    int clearSseAccountInfo();

    /**
     * 查询pbuId对应的账户数量
     * @param pbuId
     * @return 账户数量
     */
    public Long countPbuIdAccountNumber(String pbuId);

    public List<String> selectAccountListByPbuId(String bizPbuId,Long accountPerPbu);
}
