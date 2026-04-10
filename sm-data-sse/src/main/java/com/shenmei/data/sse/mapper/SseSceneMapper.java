package com.shenmei.data.sse.mapper;

import java.util.List;
import com.shenmei.data.sse.domain.SseScene;

/**
 * 测试场景Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-03
 */
public interface SseSceneMapper 
{
    /**
     * 查询测试场景
     * 
     * @param sceneId 测试场景主键
     * @return 测试场景
     */
    public SseScene selectSseSceneBySceneId(Long sceneId);

    /**
     * 查询测试场景列表
     * 
     * @param sseScene 测试场景
     * @return 测试场景集合
     */
    public List<SseScene> selectSseSceneList(SseScene sseScene);

    /**
     * 新增测试场景
     * 
     * @param sseScene 测试场景
     * @return 结果
     */
    public int insertSseScene(SseScene sseScene);

    /**
     * 修改测试场景
     * 
     * @param sseScene 测试场景
     * @return 结果
     */
    public int updateSseScene(SseScene sseScene);

    /**
     * 删除测试场景
     * 
     * @param sceneId 测试场景主键
     * @return 结果
     */
    public int deleteSseSceneBySceneId(Long sceneId);

    /**
     * 批量删除测试场景
     * 
     * @param sceneIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSseSceneBySceneIds(Long[] sceneIds);
}
