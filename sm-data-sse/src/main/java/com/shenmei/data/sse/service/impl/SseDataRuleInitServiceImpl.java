package com.shenmei.data.sse.service.impl;

import com.shenmei.data.common.utils.DateUtils;
import com.shenmei.data.common.utils.SecurityUtils;
import com.shenmei.data.sse.domain.SseDataRuleInit;
import com.shenmei.data.sse.mapper.SseDataRuleInitMapper;
import com.shenmei.data.sse.service.ISseDataRuleInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 规则初始化Service业务层处理
 *
 * @author ruoyi
 * @date 2025-08-02
 */
@Service
public class SseDataRuleInitServiceImpl implements ISseDataRuleInitService {
    @Autowired
    private SseDataRuleInitMapper sseDataRuleInitMapper;

    /**
     * 查询规则初始化
     *
     * @param pkId 规则初始化主键
     * @return 规则初始化
     */
    @Override
    public SseDataRuleInit selectSseDataRuleInitByPkId(Long pkId) {
        return sseDataRuleInitMapper.selectSseDataRuleInitByPkId(pkId);
    }

    /**
     * 查询规则初始化列表
     *
     * @param sseDataRuleInit 规则初始化
     * @return 规则初始化
     */
    @Override
    public List<SseDataRuleInit> selectSseDataRuleInitList(SseDataRuleInit sseDataRuleInit) {
        return sseDataRuleInitMapper.selectSseDataRuleInitList(sseDataRuleInit);
    }

    /**
     * 新增规则初始化
     *
     * @param sseDataRuleInit 规则初始化
     * @return 结果
     */
    @Override
    public int insertSseDataRuleInit(SseDataRuleInit sseDataRuleInit) {
        sseDataRuleInit.setCreateBy(SecurityUtils.getUsername());
        sseDataRuleInit.setCreateTime(DateUtils.getNowDate());
        return sseDataRuleInitMapper.insertSseDataRuleInit(sseDataRuleInit);
    }

    /**
     * 修改规则初始化
     *
     * @param sseDataRuleInit 规则初始化
     * @return 结果
     */
    @Override
    public int updateSseDataRuleInit(SseDataRuleInit sseDataRuleInit) {
        sseDataRuleInit.setUpdateBy(SecurityUtils.getUsername());
        sseDataRuleInit.setUpdateTime(DateUtils.getNowDate());
        return sseDataRuleInitMapper.updateSseDataRuleInit(sseDataRuleInit);
    }

    /**
     * 批量删除规则初始化
     *
     * @param pkIds 需要删除的规则初始化主键
     * @return 结果
     */
    @Override
    public int deleteSseDataRuleInitByPkIds(Long[] pkIds) {
        return sseDataRuleInitMapper.deleteSseDataRuleInitByPkIds(pkIds);
    }

    /**
     * 删除规则初始化信息
     *
     * @param pkId 规则初始化主键
     * @return 结果
     */
    @Override
    public int deleteSseDataRuleInitByPkId(Long pkId) {
        return sseDataRuleInitMapper.deleteSseDataRuleInitByPkId(pkId);
    }

    /**
     * 查询规则初始化
     *
     * @param ruleId 规则iD
     * @return 规则初始化
     */
    public SseDataRuleInit selectSseDataRuleInitByRuleId(Long ruleId) {
        return sseDataRuleInitMapper.selectSseDataRuleInitByRuleId(ruleId);
    }
}
