package com.shenmei.data.sse.mapper;

import java.util.List;
import com.shenmei.data.sse.domain.SseModel;

/**
 * 数据模型Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-03
 */
public interface SseModelMapper 
{
    /**
     * 查询数据模型
     * 
     * @param modelId 数据模型主键
     * @return 数据模型
     */
    public SseModel selectSseModelByModelId(String modelId);

    /**
     * 查询数据模型列表
     * 
     * @param sseModel 数据模型
     * @return 数据模型集合
     */
    public List<SseModel> selectSseModelList(SseModel sseModel);

    /**
     * 新增数据模型
     * 
     * @param sseModel 数据模型
     * @return 结果
     */
    public int insertSseModel(SseModel sseModel);

    /**
     * 修改数据模型
     * 
     * @param sseModel 数据模型
     * @return 结果
     */
    public int updateSseModel(SseModel sseModel);

    /**
     * 删除数据模型
     * 
     * @param modelId 数据模型主键
     * @return 结果
     */
    public int deleteSseModelByModelId(String modelId);

    /**
     * 批量删除数据模型
     * 
     * @param modelIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSseModelByModelIds(String[] modelIds);
}
