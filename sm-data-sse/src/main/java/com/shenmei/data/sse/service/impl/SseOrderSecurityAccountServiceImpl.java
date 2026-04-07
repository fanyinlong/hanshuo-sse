package com.shenmei.data.sse.service.impl;

import java.util.List;

import com.shenmei.data.sse.dataservice.ruledata.OrderSecurityAccountCalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shenmei.data.sse.mapper.SseOrderSecurityAccountMapper;
import com.shenmei.data.sse.domain.SseOrderSecurityAccount;
import com.shenmei.data.sse.service.ISseOrderSecurityAccountService;

import javax.annotation.Resource;

/**
 * 加持仓统计Service业务层处理
 *
 * @author Song
 * @date 2025-08-13
 */
@Service
public class SseOrderSecurityAccountServiceImpl implements ISseOrderSecurityAccountService
{
    @Autowired
    private SseOrderSecurityAccountMapper sseOrderSecurityAccountMapper;

    @Resource
    private OrderSecurityAccountCalService orderSecurityAccountCalService;

    /**
     * 查询加持仓统计
     *
     * @param pkId 加持仓统计主键
     * @return 加持仓统计
     */
    @Override
    public SseOrderSecurityAccount selectSseOrderSecurityAccountByPkId(Long pkId)
    {
        return sseOrderSecurityAccountMapper.selectSseOrderSecurityAccountByPkId(pkId);
    }

    /**
     * 查询加持仓统计列表
     *
     * @param sseOrderSecurityAccount 加持仓统计
     * @return 加持仓统计
     */
    @Override
    public List<SseOrderSecurityAccount> selectSseOrderSecurityAccountList(SseOrderSecurityAccount sseOrderSecurityAccount)
    {
        return sseOrderSecurityAccountMapper.selectSseOrderSecurityAccountList(sseOrderSecurityAccount);
    }

    /**
     * 新增加持仓统计
     *
     * @param sseOrderSecurityAccount 加持仓统计
     * @return 结果
     */
    @Override
    public int insertSseOrderSecurityAccount(SseOrderSecurityAccount sseOrderSecurityAccount)
    {
//        return sseOrderSecurityAccountMapper.insertSseOrderSecurityAccount(sseOrderSecurityAccount);
        return orderSecurityAccountCalService.calOrderSecurityAccount(sseOrderSecurityAccount.getRuleId());
    }

    /**
     * 修改加持仓统计
     *
     * @param sseOrderSecurityAccount 加持仓统计
     * @return 结果
     */
    @Override
    public int updateSseOrderSecurityAccount(SseOrderSecurityAccount sseOrderSecurityAccount)
    {
        return sseOrderSecurityAccountMapper.updateSseOrderSecurityAccount(sseOrderSecurityAccount);
    }

    /**
     * 批量删除加持仓统计
     *
     * @param pkIds 需要删除的加持仓统计主键
     * @return 结果
     */
    @Override
    public int deleteSseOrderSecurityAccountByPkIds(Long[] pkIds)
    {
        return sseOrderSecurityAccountMapper.deleteSseOrderSecurityAccountByPkIds(pkIds);
    }

    /**
     * 删除加持仓统计信息
     *
     * @param pkId 加持仓统计主键
     * @return 结果
     */
    @Override
    public int deleteSseOrderSecurityAccountByPkId(Long pkId)
    {
        return sseOrderSecurityAccountMapper.deleteSseOrderSecurityAccountByPkId(pkId);
    }

    @Override
    public int batchInsertSseOrderSecurityAccount(List<SseOrderSecurityAccount> sseOrderSecurityAccountList){
        return sseOrderSecurityAccountMapper.batchInsertSseOrderSecurityAccount(sseOrderSecurityAccountList);
    }
}
