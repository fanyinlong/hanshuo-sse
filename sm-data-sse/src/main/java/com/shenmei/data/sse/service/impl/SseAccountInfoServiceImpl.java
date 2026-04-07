package com.shenmei.data.sse.service.impl;

import com.shenmei.data.sse.dataservice.filedata.SseAccountInfoBatchImportService;
import com.shenmei.data.sse.domain.SseAccountInfo;
import com.shenmei.data.sse.mapper.SseAccountInfoMapper;
import com.shenmei.data.sse.service.ISseAccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 账户信息Service业务层处理
 *
 * @author song
 * @date 2025-07-28
 */
@Service
public class SseAccountInfoServiceImpl implements ISseAccountInfoService
{
    @Autowired
    private SseAccountInfoMapper sseAccountInfoMapper;

    @Resource
    SseAccountInfoBatchImportService sseAccountInfoBatchImportService;

    /**
     * 查询账户信息
     *
     * @param pkId 账户信息主键
     * @return 账户信息
     */
    @Override
    public SseAccountInfo selectSseAccountInfoByPkId(Long pkId)
    {
        return sseAccountInfoMapper.selectSseAccountInfoByPkId(pkId);
    }

    /**
     * 查询账户信息列表
     *
     * @param sseAccountInfo 账户信息
     * @return 账户信息
     */
    @Override
    public List<SseAccountInfo> selectSseAccountInfoList(SseAccountInfo sseAccountInfo)
    {
        return sseAccountInfoMapper.selectSseAccountInfoList(sseAccountInfo);
    }

    /**
     * 新增账户信息
     *
     * @param sseAccountInfo 账户信息
     * @return 结果
     */
    @Override
    public int insertSseAccountInfo(SseAccountInfo sseAccountInfo)
    {
        return sseAccountInfoMapper.insertSseAccountInfo(sseAccountInfo);
    }

    /**
     * 修改账户信息
     *
     * @param sseAccountInfo 账户信息
     * @return 结果
     */
    @Override
    public int updateSseAccountInfo(SseAccountInfo sseAccountInfo)
    {
        return sseAccountInfoMapper.updateSseAccountInfo(sseAccountInfo);
    }

    /**
     * 批量删除账户信息
     *
     * @param pkIds 需要删除的账户信息主键
     * @return 结果
     */
    @Override
    public int deleteSseAccountInfoByPkIds(Long[] pkIds)
    {
        return sseAccountInfoMapper.deleteSseAccountInfoByPkIds(pkIds);
    }

    /**
     * 删除账户信息信息
     *
     * @param pkId 账户信息主键
     * @return 结果
     */
    @Override
    public int deleteSseAccountInfoByPkId(Long pkId)
    {
        return sseAccountInfoMapper.deleteSseAccountInfoByPkId(pkId);
    }

    @Override
    public int batchInsertSseAccountInfo(List<SseAccountInfo> sseAccountInfoList)  {
        return sseAccountInfoMapper.batchInsertSseAccountInfo(sseAccountInfoList);
    }


    @Override
    public void init() {
        sseAccountInfoBatchImportService.batchImportAccountInfo();
    }
    @Override
    public int clearSseAccountInfo() {
        return sseAccountInfoMapper.clearSseAccountInfo();
    }

    @Override
    public Long countPbuIdAccountNumber(String pbuId) {
        return sseAccountInfoMapper.countPbuIdAccountNumber(pbuId);
    }

    @Override
    public List<String> selectAccountListByPbuId(String bizPbuId,Long accountPerPbu) {
        return sseAccountInfoMapper.selectAccountListByPbuId(bizPbuId,accountPerPbu);
    }
}
