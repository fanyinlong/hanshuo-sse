package com.shenmei.data.sse.mapper;

import java.util.List;
import com.shenmei.data.sse.domain.SseModelParam;

/**
 * 模型参数关联Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-03
 */
public interface SseModelParamMapper 
{
    /**
     * 查询模型参数关联
     * 
     * @param id 模型参数关联主键
     * @return 模型参数关联
     */
    public SseModelParam selectSseModelParamById(Long id);

    /**
     * 查询模型参数关联列表
     * 
     * @param sseModelParam 模型参数关联
     * @return 模型参数关联集合
     */
    public List<SseModelParam> selectSseModelParamList(SseModelParam sseModelParam);

    /**
     * 新增模型参数关联
     * 
     * @param sseModelParam 模型参数关联
     * @return 结果
     */
    public int insertSseModelParam(SseModelParam sseModelParam);

    /**
     * 修改模型参数关联
     * 
     * @param sseModelParam 模型参数关联
     * @return 结果
     */
    public int updateSseModelParam(SseModelParam sseModelParam);

    /**
     * 删除模型参数关联
     * 
     * @param id 模型参数关联主键
     * @return 结果
     */
    public int deleteSseModelParamById(Long id);

    /**
     * 批量删除模型参数关联
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSseModelParamByIds(Long[] ids);
}
