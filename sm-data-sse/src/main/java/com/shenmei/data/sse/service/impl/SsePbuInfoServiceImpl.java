package com.shenmei.data.sse.service.impl;

import com.shenmei.data.sse.dataservice.filedata.SsePbuInfoBatchImportService;
import com.shenmei.data.sse.domain.SsePbuInfo;
import com.shenmei.data.sse.mapper.SsePbuInfoMapper;
import com.shenmei.data.sse.service.ISsePbuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * PBU信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-07-27
 */
@Service
public class SsePbuInfoServiceImpl implements ISsePbuInfoService
{
    @Autowired
    private SsePbuInfoMapper ssePbuInfoMapper;

    @Resource
    SsePbuInfoBatchImportService ssePbuInfoBatchImportService;

    /**
     * 查询PBU信息
     * 
     * @param pbuId PBU信息主键
     * @return PBU信息
     */
    @Override
    public SsePbuInfo selectSsePbuInfoByPbuId(String pbuId)
    {
        return ssePbuInfoMapper.selectSsePbuInfoByPbuId(pbuId);
    }

    /**
     * 查询PBU信息列表
     * 
     * @param ssePbuInfo PBU信息
     * @return PBU信息
     */
    @Override
    public List<SsePbuInfo> selectSsePbuInfoList(SsePbuInfo ssePbuInfo)
    {
        return ssePbuInfoMapper.selectSsePbuInfoList(ssePbuInfo);
    }

    /**
     * 新增PBU信息
     * 
     * @param ssePbuInfo PBU信息
     * @return 结果
     */
    @Override
    public int insertSsePbuInfo(SsePbuInfo ssePbuInfo)
    {
        return ssePbuInfoMapper.insertSsePbuInfo(ssePbuInfo);
    }

    /**
     * 修改PBU信息
     * 
     * @param ssePbuInfo PBU信息
     * @return 结果
     */
    @Override
    public int updateSsePbuInfo(SsePbuInfo ssePbuInfo)
    {
        return ssePbuInfoMapper.updateSsePbuInfo(ssePbuInfo);
    }

    /**
     * 批量删除PBU信息
     * 
     * @param pbuIds 需要删除的PBU信息主键
     * @return 结果
     */
    @Override
    public int deleteSsePbuInfoByPbuIds(String[] pbuIds)
    {
        return ssePbuInfoMapper.deleteSsePbuInfoByPbuIds(pbuIds);
    }

    /**
     * 删除PBU信息信息
     * 
     * @param pbuId PBU信息主键
     * @return 结果
     */
    @Override
    public int deleteSsePbuInfoByPbuId(String pbuId)
    {
        return ssePbuInfoMapper.deleteSsePbuInfoByPbuId(pbuId);
    }

    @Override
    public int batchInsertSsePbuInfo(List<SsePbuInfo> ssePbuInfoList) {
        return ssePbuInfoMapper.batchInsertSsePbuInfo(ssePbuInfoList);
    }

    @Override
    public void init() {
         ssePbuInfoBatchImportService.batchImportPbuInfo();
    }

    @Override
    public int clearSsePbuInfo() {
        return ssePbuInfoMapper.clearSsePbuInfo();
    }

    @Override
    public List<String> selectAllSsePbuIds() {
        return ssePbuInfoMapper.selectAllSsePbuIds();
    }
}
