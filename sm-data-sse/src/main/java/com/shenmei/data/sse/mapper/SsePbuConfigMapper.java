package com.shenmei.data.sse.mapper;

import java.util.List;
import com.shenmei.data.sse.domain.SsePbuConfig;
import org.apache.ibatis.annotations.Param;

/**
 * PBU配置Mapper接口
 *
 * @author Song
 * @date 2025-08-01
 */
public interface SsePbuConfigMapper
{
    /**
     * 查询PBU配置
     *
     * @param pkId PBU配置主键
     * @return PBU配置
     */
    public SsePbuConfig selectSsePbuConfigByPkId(Long pkId);

    /**
     * 查询PBU配置列表
     *
     * @param ssePbuConfig PBU配置
     * @return PBU配置集合
     */
    public List<SsePbuConfig> selectSsePbuConfigList(SsePbuConfig ssePbuConfig);

    /**
     * 新增PBU配置
     *
     * @param ssePbuConfig PBU配置
     * @return 结果
     */
    public int insertSsePbuConfig(SsePbuConfig ssePbuConfig);

    /**
     * 修改PBU配置
     *
     * @param ssePbuConfig PBU配置
     * @return 结果
     */
    public int updateSsePbuConfig(SsePbuConfig ssePbuConfig);

    /**
     * 删除PBU配置
     *
     * @param pkId PBU配置主键
     * @return 结果
     */
    public int deleteSsePbuConfigByPkId(Long pkId);

    /**
     * 批量删除PBU配置
     *
     * @param pkIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSsePbuConfigByPkIds(Long[] pkIds);

    /**
     * 清空sse_pbu_config表所有数据
     *
     * @return 删除的记录数
     */
    public int clearSsePbuConfig();

/**
     * 批量插入PBU信息
     * @param ssePbuConfigList PBU信息列表
     * @return 插入记录数
     */
    public int batchInsertSsePbuConfig(List<SsePbuConfig> ssePbuConfigList);

    public List<String> selectLoginPbuIdList(Long loginPbuNumber);

    public List<String> selectBizPbuIdList(@Param("bizPbuNumber") Long bizPbuNumber, @Param("accountNumber") Long accountNumber);


}
