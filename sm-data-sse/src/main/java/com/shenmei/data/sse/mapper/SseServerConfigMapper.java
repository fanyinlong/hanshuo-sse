package com.shenmei.data.sse.mapper;

import com.shenmei.data.sse.domain.SseServerConfig;

import java.util.List;

/**
 * 执行机分发配置Mapper接口
 * 
 * @author ruoyi
 * @date 2026-02-12
 */
public interface SseServerConfigMapper 
{
    /**
     * 查询执行机分发配置
     * 
     * @param id 执行机分发配置主键
     * @return 执行机分发配置
     */
    public SseServerConfig selectSseServerConfigById(Long id);

    /**
     * 查询执行机分发配置列表
     * 
     * @param sseServerConfig 执行机分发配置
     * @return 执行机分发配置集合
     */
    public List<SseServerConfig> selectSseServerConfigList(SseServerConfig sseServerConfig);

    /**
     * 新增执行机分发配置
     * 
     * @param sseServerConfig 执行机分发配置
     * @return 结果
     */
    public int insertSseServerConfig(SseServerConfig sseServerConfig);

    /**
     * 修改执行机分发配置
     * 
     * @param sseServerConfig 执行机分发配置
     * @return 结果
     */
    public int updateSseServerConfig(SseServerConfig sseServerConfig);

    /**
     * 删除执行机分发配置
     * 
     * @param id 执行机分发配置主键
     * @return 结果
     */
    public int deleteSseServerConfigById(Long id);

    /**
     * 批量删除执行机分发配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSseServerConfigByIds(Long[] ids);

    public List<SseServerConfig> selectSseServerConfigByRuleId(Long id);

}
