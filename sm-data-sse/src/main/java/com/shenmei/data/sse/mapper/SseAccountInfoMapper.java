package com.shenmei.data.sse.mapper;

import java.util.List;
import com.shenmei.data.sse.domain.SseAccountInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 账户信息Mapper接口
 *
 * @author song
 * @date 2025-08-01
 */
public interface SseAccountInfoMapper
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
     * 删除账户信息
     *
     * @param pkId 账户信息主键
     * @return 结果
     */
    public int deleteSseAccountInfoByPkId(Long pkId);

    /**
     * 批量删除账户信息
     *
     * @param pkIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSseAccountInfoByPkIds(Long[] pkIds);


/**
     * 批量插入产品信息
     * @param sseAccountInfoList 产品信息列表
     * @return 插入记录数
     */
    public int batchInsertSseAccountInfo(List<SseAccountInfo> sseAccountInfoList);

    /**
     * 清空sse_account_info表所有数据
     *
     * @return 删除的记录数
     */
    int clearSseAccountInfo();

    /**
     * 返回pbuId对应的账户数量
     * @param pbuId
     * @return 账户数量
     */
    public Long countPbuIdAccountNumber(String pbuId);

    /**
     * 根据pbuId查询账户信息
     * @param bizPbuId,accountPerPbu, bizPbuId:pbuId,accountPerPbu:每个pbu对应的账户数量
     * @return 账户信息
     */
    public List<String> selectAccountListByPbuId(@Param("bizPbuId") String bizPbuId, @Param("accountPerPbu") Long accountPerPbu);
}
