package com.shenmei.data.sse.service.impl;


import com.shenmei.data.sse.dataservice.ruledata.SsePbuConfigInitService;
import com.shenmei.data.sse.domain.SsePbuConfig;
import com.shenmei.data.sse.mapper.SsePbuConfigMapper;
import com.shenmei.data.sse.service.ISsePbuConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * PBU配置Service业务层处理
 *
 * @author Song
 * @date 2025-08-01
 */
@Service
public class SsePbuConfigServiceImpl implements ISsePbuConfigService
{
    @Autowired
    private SsePbuConfigMapper ssePbuConfigMapper;
    @Resource
    SsePbuConfigInitService ssePbuConfigInitService;

    /**
     * 查询PBU配置
     *
     * @param pkId PBU配置主键
     * @return PBU配置
     */
    @Override
    public SsePbuConfig selectSsePbuConfigByPkId(Long pkId)
    {
        return ssePbuConfigMapper.selectSsePbuConfigByPkId(pkId);
    }

    /**
     * 查询PBU配置列表
     *
     * @param ssePbuConfig PBU配置
     * @return PBU配置
     */
    @Override
    public List<SsePbuConfig> selectSsePbuConfigList(SsePbuConfig ssePbuConfig)
    {
        return ssePbuConfigMapper.selectSsePbuConfigList(ssePbuConfig);
    }

    /**
     * 新增PBU配置
     *
     * @param ssePbuConfig PBU配置
     * @return 结果
     */
    @Override
    public int insertSsePbuConfig(SsePbuConfig ssePbuConfig)
    {
        return ssePbuConfigMapper.insertSsePbuConfig(ssePbuConfig);
    }

    /**
     * 修改PBU配置
     *
     * @param ssePbuConfig PBU配置
     * @return 结果
     */
    @Override
    public int updateSsePbuConfig(SsePbuConfig ssePbuConfig)
    {
        return ssePbuConfigMapper.updateSsePbuConfig(ssePbuConfig);
    }

    /**
     * 批量删除PBU配置
     *
     * @param pkIds 需要删除的PBU配置主键
     * @return 结果
     */
    @Override
    public int deleteSsePbuConfigByPkIds(Long[] pkIds)
    {
        return ssePbuConfigMapper.deleteSsePbuConfigByPkIds(pkIds);
    }

    /**
     * 删除PBU配置信息
     *
     * @param pkId PBU配置主键
     * @return 结果
     */
    @Override
    public int deleteSsePbuConfigByPkId(Long pkId)
    {
        return ssePbuConfigMapper.deleteSsePbuConfigByPkId(pkId);
    }


@Override
    public int batchInsertSsePbuConfig(List<SsePbuConfig> ssePBUConfigList) {
        return ssePbuConfigMapper.batchInsertSsePbuConfig(ssePBUConfigList);
    }

    @Override
    public void init() {
        ssePbuConfigInitService.initPbuConfig();
    }

    @Override
    public int clearSsePbuConfig() {
        return ssePbuConfigMapper.clearSsePbuConfig();
    }

    @Override
    public List<String> selectLoginPbuIdList(Long loginPbuNumber) {
        return ssePbuConfigMapper.selectLoginPbuIdList(loginPbuNumber);
    }

    @Override
    public List<String> selectBizPbuIdList(Long bizPbuNumber, Long accountNumber) {
        return ssePbuConfigMapper.selectBizPbuIdList(bizPbuNumber, accountNumber);
    }
}
