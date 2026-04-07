package com.shenmei.data.sse.service;

import java.util.List;
import com.shenmei.data.sse.domain.SsePbuInfo;

/**
 * PBU信息Service接口
 * 
 * @author ruoyi
 * @date 2025-07-27
 */
public interface ISsePbuInfoService 
{
    /**
     * 查询PBU信息
     * 
     * @param pbuId PBU信息主键
     * @return PBU信息
     */
    public SsePbuInfo selectSsePbuInfoByPbuId(String pbuId);

    /**
     * 查询PBU信息列表
     * 
     * @param ssePbuInfo PBU信息
     * @return PBU信息集合
     */
    public List<SsePbuInfo> selectSsePbuInfoList(SsePbuInfo ssePbuInfo);

    /**
     * 新增PBU信息
     * 
     * @param ssePbuInfo PBU信息
     * @return 结果
     */
    public int insertSsePbuInfo(SsePbuInfo ssePbuInfo);

    /**
     * 修改PBU信息
     * 
     * @param ssePbuInfo PBU信息
     * @return 结果
     */
    public int updateSsePbuInfo(SsePbuInfo ssePbuInfo);

    /**
     * 批量删除PBU信息
     * 
     * @param pbuIds 需要删除的PBU信息主键集合
     * @return 结果
     */
    public int deleteSsePbuInfoByPbuIds(String[] pbuIds);

    /**
     * 删除PBU信息信息
     * 
     * @param pbuId PBU信息主键
     * @return 结果
     */
    public int deleteSsePbuInfoByPbuId(String pbuId);

    /**
     * 批量插入PBU信息
     * @param ssePbuInfoList PBU信息列表
     * @return 插入记录数
     */
    int batchInsertSsePbuInfo(List<SsePbuInfo> ssePbuInfoList);

    /**
     * 初始化PBU信息
     */
    void init();

    /**
     * 清空所有PBU信息
     *
     * @return 删除的记录数
     */
    int clearSsePbuInfo();

    /**
     * 查询pbuIds
     * @return pbuIds
     */
    List<String> selectAllSsePbuIds();
}
