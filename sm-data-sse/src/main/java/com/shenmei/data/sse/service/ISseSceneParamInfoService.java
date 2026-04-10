package com.shenmei.data.sse.service;

import java.util.List;
import com.shenmei.data.sse.domain.SseSceneParamInfo;

/**
 * 场景参数信息Service接口
 * 
 * @author ruoyi
 * @date 2026-04-03
 */
public interface ISseSceneParamInfoService 
{
    /**
     * 查询场景参数信息
     * 
     * @param paramInfoId 场景参数信息主键
     * @return 场景参数信息
     */
    public SseSceneParamInfo selectSseSceneParamInfoByParamInfoId(Long paramInfoId);

    /**
     * 查询场景参数信息列表
     * 
     * @param sseSceneParamInfo 场景参数信息
     * @return 场景参数信息集合
     */
    public List<SseSceneParamInfo> selectSseSceneParamInfoList(SseSceneParamInfo sseSceneParamInfo);

    /**
     * 新增场景参数信息
     * 
     * @param sseSceneParamInfo 场景参数信息
     * @return 结果
     */
    public int insertSseSceneParamInfo(SseSceneParamInfo sseSceneParamInfo);

    /**
     * 修改场景参数信息
     * 
     * @param sseSceneParamInfo 场景参数信息
     * @return 结果
     */
    public int updateSseSceneParamInfo(SseSceneParamInfo sseSceneParamInfo);

    /**
     * 批量删除场景参数信息
     * 
     * @param paramInfoIds 需要删除的场景参数信息主键集合
     * @return 结果
     */
    public int deleteSseSceneParamInfoByParamInfoIds(Long[] paramInfoIds);

    /**
     * 删除场景参数信息信息
     * 
     * @param paramInfoId 场景参数信息主键
     * @return 结果
     */
    public int deleteSseSceneParamInfoByParamInfoId(Long paramInfoId);
}
