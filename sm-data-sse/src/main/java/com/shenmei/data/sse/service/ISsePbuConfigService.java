package com.shenmei.data.sse.service;

import java.util.List;
import com.shenmei.data.sse.domain.SsePbuConfig;

/**
 * PBU配置Service接口
 *
 * @author Song
 * @date 2025-08-01
 */
public interface ISsePbuConfigService
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
     * 批量删除PBU配置
     *
     * @param pkIds 需要删除的PBU配置主键集合
     * @return 结果
     */
    public int deleteSsePbuConfigByPkIds(Long[] pkIds);

    /**
     * 删除PBU配置信息
     *
     * @param pkId PBU配置主键
     * @return 结果
     */
    public int deleteSsePbuConfigByPkId(Long pkId);


/**
     * 批量插入PBU配置
     * @param ssePbuConfigList PBU配置列表
     * @return 插入记录数
     */
    int batchInsertSsePbuConfig(List<SsePbuConfig> ssePbuConfigList);

    /**
     * 初始化PBU配置
     */
    void init();

    /**
     * 清空所有PBU配置
     *
     * @return 删除的记录数
     */
    int clearSsePbuConfig();

    /**
     * 根据AccountNumber查询登陆PBU
     * @param loginPbuNumber
     * @return PbuIdList
     */
    List<String> selectLoginPbuIdList(Long loginPbuNumber);


    /**
     * 根据AccountNumber查询登陆PBU
     * @param bizPbuNumber,bizPbuNumber
     * @return PbuIdList
     */
    List<String> selectBizPbuIdList(Long bizPbuNumber, Long accountNumber);
}

