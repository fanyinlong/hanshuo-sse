package com.shenmei.data.sse.service.impl;

import java.util.List;

import com.shenmei.data.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shenmei.data.sse.mapper.SseSceneParamInfoMapper;
import com.shenmei.data.sse.domain.SseSceneParamInfo;
import com.shenmei.data.sse.service.ISseSceneParamInfoService;

/**
 * 场景参数信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-03
 */
@Service
public class SseSceneParamInfoServiceImpl implements ISseSceneParamInfoService 
{
    @Autowired
    private SseSceneParamInfoMapper sseSceneParamInfoMapper;

    /**
     * 查询场景参数信息
     * 
     * @param paramInfoId 场景参数信息主键
     * @return 场景参数信息
     */
    @Override
    public SseSceneParamInfo selectSseSceneParamInfoByParamInfoId(Long paramInfoId)
    {
        return sseSceneParamInfoMapper.selectSseSceneParamInfoByParamInfoId(paramInfoId);
    }

    /**
     * 查询场景参数信息列表
     * 
     * @param sseSceneParamInfo 场景参数信息
     * @return 场景参数信息
     */
    @Override
    public List<SseSceneParamInfo> selectSseSceneParamInfoList(SseSceneParamInfo sseSceneParamInfo)
    {
        return sseSceneParamInfoMapper.selectSseSceneParamInfoList(sseSceneParamInfo);
    }

    /**
     * 新增场景参数信息
     * 
     * @param sseSceneParamInfo 场景参数信息
     * @return 结果
     */
    @Override
    public int insertSseSceneParamInfo(SseSceneParamInfo sseSceneParamInfo)
    {
        sseSceneParamInfo.setCreateTime(DateUtils.getNowDate());
        return sseSceneParamInfoMapper.insertSseSceneParamInfo(sseSceneParamInfo);
    }

    /**
     * 修改场景参数信息
     * 
     * @param sseSceneParamInfo 场景参数信息
     * @return 结果
     */
    @Override
    public int updateSseSceneParamInfo(SseSceneParamInfo sseSceneParamInfo)
    {
        sseSceneParamInfo.setUpdateTime(DateUtils.getNowDate());
        return sseSceneParamInfoMapper.updateSseSceneParamInfo(sseSceneParamInfo);
    }

    /**
     * 批量删除场景参数信息
     * 
     * @param paramInfoIds 需要删除的场景参数信息主键
     * @return 结果
     */
    @Override
    public int deleteSseSceneParamInfoByParamInfoIds(Long[] paramInfoIds)
    {
        return sseSceneParamInfoMapper.deleteSseSceneParamInfoByParamInfoIds(paramInfoIds);
    }

    /**
     * 删除场景参数信息信息
     * 
     * @param paramInfoId 场景参数信息主键
     * @return 结果
     */
    @Override
    public int deleteSseSceneParamInfoByParamInfoId(Long paramInfoId)
    {
        return sseSceneParamInfoMapper.deleteSseSceneParamInfoByParamInfoId(paramInfoId);
    }
}
