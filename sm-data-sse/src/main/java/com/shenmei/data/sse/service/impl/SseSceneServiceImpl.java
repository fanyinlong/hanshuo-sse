package com.shenmei.data.sse.service.impl;

import java.util.List;

import com.shenmei.data.common.utils.DateUtils;
import com.shenmei.data.sse.mapper.SseSceneParamInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shenmei.data.sse.mapper.SseSceneMapper;
import com.shenmei.data.sse.domain.SseScene;
import com.shenmei.data.sse.service.ISseSceneService;

/**
 * 测试场景Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-03
 */
@Service
public class SseSceneServiceImpl implements ISseSceneService 
{
    @Autowired
    private SseSceneMapper sseSceneMapper;

    @Autowired
    private SseSceneParamInfoMapper sseSceneParamInfoMapper;

    /**
     * 查询测试场景
     * 
     * @param sceneId 测试场景主键
     * @return 测试场景
     */
    @Override
    public SseScene selectSseSceneBySceneId(Long sceneId)
    {
        return sseSceneMapper.selectSseSceneBySceneId(sceneId);
    }

    /**
     * 查询测试场景列表
     * 
     * @param sseScene 测试场景
     * @return 测试场景
     */
    @Override
    public List<SseScene> selectSseSceneList(SseScene sseScene)
    {
        return sseSceneMapper.selectSseSceneList(sseScene);
    }

    /**
     * 新增测试场景
     * 
     * @param sseScene 测试场景
     * @return 结果
     */
    @Override
    public int insertSseScene(SseScene sseScene)
    {
        sseScene.setCreateTime(DateUtils.getNowDate());
        return sseSceneMapper.insertSseScene(sseScene);
    }

    /**
     * 修改测试场景
     * 
     * @param sseScene 测试场景
     * @return 结果
     */
    @Override
    public int updateSseScene(SseScene sseScene)
    {
        sseScene.setUpdateTime(DateUtils.getNowDate());
        return sseSceneMapper.updateSseScene(sseScene);
    }

    /**
     * 批量删除测试场景
     * 
     * @param sceneIds 需要删除的测试场景主键
     * @return 结果
     */
    @Override
    public int deleteSseSceneBySceneIds(Long[] sceneIds)
    {
        sseSceneMapper.deleteSseSceneBySceneIds(sceneIds);

        return sseSceneParamInfoMapper.deleteSseSceneParamInfoBySceneIds(sceneIds);

    }

    /**
     * 删除测试场景信息
     * 
     * @param sceneId 测试场景主键
     * @return 结果
     */
    @Override
    public int deleteSseSceneBySceneId(Long sceneId)
    {


        return sseSceneMapper.deleteSseSceneBySceneId(sceneId);
    }
}
