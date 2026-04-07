package com.shenmei.data.sse.mapper;

import java.util.List;
import com.shenmei.data.sse.domain.SsePbuInfo;

/**
 * PBU信息Mapper接口
 * 
 * @author ruoyi
 * @date 2025-07-27
 */
public interface SsePbuInfoMapper 
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
     * 删除PBU信息
     * 
     * @param pbuId PBU信息主键
     * @return 结果
     */
    public int deleteSsePbuInfoByPbuId(String pbuId);

    /**
     * 批量删除PBU信息
     * 
     * @param pbuIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSsePbuInfoByPbuIds(String[] pbuIds);

    /**
     * 批量插入PBU信息
     * @param ssePbuInfoList PBU信息列表
     * @return 插入记录数
     */
    int batchInsertSsePbuInfo(List<SsePbuInfo> ssePbuInfoList);

    /**
     * 清空sse_pbu_info表所有数据
     *
     * @return 删除的记录数
     */
    int clearSsePbuInfo();

    /**
     * 批量选取PBUID信息
     *
     * @return 插入记录数
     */
    public List<String> selectAllSsePbuIds();


}
