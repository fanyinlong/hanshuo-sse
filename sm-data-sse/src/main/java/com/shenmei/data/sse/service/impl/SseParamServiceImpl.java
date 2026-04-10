package com.shenmei.data.sse.service.impl;

import java.util.List;

import com.shenmei.data.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shenmei.data.sse.mapper.SseParamMapper;
import com.shenmei.data.sse.domain.SseParam;
import com.shenmei.data.sse.service.ISseParamService;

/**
 * 参数定义Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-03
 */
@Service
public class SseParamServiceImpl implements ISseParamService 
{
    @Autowired
    private SseParamMapper sseParamMapper;

    /**
     * 查询参数定义
     * 
     * @param paramId 参数定义主键
     * @return 参数定义
     */
    @Override
    public SseParam selectSseParamByParamId(String paramId)
    {
        return sseParamMapper.selectSseParamByParamId(paramId);
    }

    /**
     * 查询参数定义列表
     * 
     * @param sseParam 参数定义
     * @return 参数定义
     */
    @Override
    public List<SseParam> selectSseParamList(SseParam sseParam)
    {
        return sseParamMapper.selectSseParamList(sseParam);
    }

    /**
     * 新增参数定义
     * 
     * @param sseParam 参数定义
     * @return 结果
     */
    @Override
    public int insertSseParam(SseParam sseParam)
    {
        sseParam.setCreateTime(DateUtils.getNowDate());
        return sseParamMapper.insertSseParam(sseParam);
    }

    /**
     * 修改参数定义
     * 
     * @param sseParam 参数定义
     * @return 结果
     */
    @Override
    public int updateSseParam(SseParam sseParam)
    {
        sseParam.setUpdateTime(DateUtils.getNowDate());
        return sseParamMapper.updateSseParam(sseParam);
    }

    /**
     * 批量删除参数定义
     * 
     * @param paramIds 需要删除的参数定义主键
     * @return 结果
     */
    @Override
    public int deleteSseParamByParamIds(String[] paramIds)
    {
        return sseParamMapper.deleteSseParamByParamIds(paramIds);
    }

    /**
     * 删除参数定义信息
     * 
     * @param paramId 参数定义主键
     * @return 结果
     */
    @Override
    public int deleteSseParamByParamId(String paramId)
    {
        return sseParamMapper.deleteSseParamByParamId(paramId);
    }
}
