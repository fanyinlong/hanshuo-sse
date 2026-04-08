package com.shenmei.data.sse.service;

import java.util.List;
import com.shenmei.data.sse.domain.SseParam;

/**
 * 参数定义Service接口
 * 
 * @author ruoyi
 * @date 2026-04-03
 */
public interface ISseParamService 
{
    /**
     * 查询参数定义
     * 
     * @param paramId 参数定义主键
     * @return 参数定义
     */
    public SseParam selectSseParamByParamId(String paramId);

    /**
     * 查询参数定义列表
     * 
     * @param sseParam 参数定义
     * @return 参数定义集合
     */
    public List<SseParam> selectSseParamList(SseParam sseParam);

    /**
     * 新增参数定义
     * 
     * @param sseParam 参数定义
     * @return 结果
     */
    public int insertSseParam(SseParam sseParam);

    /**
     * 修改参数定义
     * 
     * @param sseParam 参数定义
     * @return 结果
     */
    public int updateSseParam(SseParam sseParam);

    /**
     * 批量删除参数定义
     * 
     * @param paramIds 需要删除的参数定义主键集合
     * @return 结果
     */
    public int deleteSseParamByParamIds(String[] paramIds);

    /**
     * 删除参数定义信息
     * 
     * @param paramId 参数定义主键
     * @return 结果
     */
    public int deleteSseParamByParamId(String paramId);
}
