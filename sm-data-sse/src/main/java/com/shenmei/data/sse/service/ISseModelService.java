package com.shenmei.data.sse.service;

import java.util.List;
import com.shenmei.data.sse.domain.SseModel;
import com.shenmei.data.sse.dto.ModelAddDo;
import com.shenmei.data.sse.dto.ModelDo;

/**
 * 数据模型Service接口
 * 
 * @author ruoyi
 * @date 2026-04-03
 */
public interface ISseModelService 
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
    public int insertSseModel(ModelDo sseModel);

    /**
     * 修改数据模型
     * 
     * @param sseModel 数据模型
     * @return 结果
     */
    public int updateSseModel(SseModel sseModel);

    /**
     * 批量删除数据模型
     * 
     * @param modelIds 需要删除的数据模型主键集合
     * @return 结果
     */
    public int deleteSseModelByModelIds(String[] modelIds);

    /**
     * 删除数据模型信息
     * 
     * @param modelId 数据模型主键
     * @return 结果
     */
    public int deleteSseModelByModelId(String modelId);

    public List<ModelAddDo> getParamList();

    public ModelDo getModel(String modelId);

}
