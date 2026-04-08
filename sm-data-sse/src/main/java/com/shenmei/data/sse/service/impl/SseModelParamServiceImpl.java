package com.shenmei.data.sse.service.impl;

import java.util.List;

import com.shenmei.data.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shenmei.data.sse.mapper.SseModelParamMapper;
import com.shenmei.data.sse.domain.SseModelParam;
import com.shenmei.data.sse.service.ISseModelParamService;

/**
 * 模型参数关联Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-03
 */
@Service
public class SseModelParamServiceImpl implements ISseModelParamService 
{
    @Autowired
    private SseModelParamMapper sseModelParamMapper;

    /**
     * 查询模型参数关联
     * 
     * @param id 模型参数关联主键
     * @return 模型参数关联
     */
    @Override
    public SseModelParam selectSseModelParamById(Long id)
    {
        return sseModelParamMapper.selectSseModelParamById(id);
    }

    /**
     * 查询模型参数关联列表
     * 
     * @param sseModelParam 模型参数关联
     * @return 模型参数关联
     */
    @Override
    public List<SseModelParam> selectSseModelParamList(SseModelParam sseModelParam)
    {
        return sseModelParamMapper.selectSseModelParamList(sseModelParam);
    }

    /**
     * 新增模型参数关联
     * 
     * @param sseModelParam 模型参数关联
     * @return 结果
     */
    @Override
    public int insertSseModelParam(SseModelParam sseModelParam)
    {
        sseModelParam.setCreateTime(DateUtils.getNowDate());
        return sseModelParamMapper.insertSseModelParam(sseModelParam);
    }

    /**
     * 修改模型参数关联
     * 
     * @param sseModelParam 模型参数关联
     * @return 结果
     */
    @Override
    public int updateSseModelParam(SseModelParam sseModelParam)
    {
        sseModelParam.setUpdateTime(DateUtils.getNowDate());
        return sseModelParamMapper.updateSseModelParam(sseModelParam);
    }

    /**
     * 批量删除模型参数关联
     * 
     * @param ids 需要删除的模型参数关联主键
     * @return 结果
     */
    @Override
    public int deleteSseModelParamByIds(Long[] ids)
    {
        return sseModelParamMapper.deleteSseModelParamByIds(ids);
    }

    /**
     * 删除模型参数关联信息
     * 
     * @param id 模型参数关联主键
     * @return 结果
     */
    @Override
    public int deleteSseModelParamById(Long id)
    {
        return sseModelParamMapper.deleteSseModelParamById(id);
    }
}
