package com.shenmei.data.sse.service;

import java.util.List;
import com.shenmei.data.sse.domain.SseOrderSecurityAccount;

/**
 * 加持仓统计Service接口
 *
 * @author Song
 * @date 2025-08-13
 */
public interface ISseOrderSecurityAccountService
{
    /**
     * 查询加持仓统计
     *
     * @param pkId 加持仓统计主键
     * @return 加持仓统计
     */
    public SseOrderSecurityAccount selectSseOrderSecurityAccountByPkId(Long pkId);

    /**
     * 查询加持仓统计列表
     *
     * @param sseOrderSecurityAccount 加持仓统计
     * @return 加持仓统计集合
     */
    public List<SseOrderSecurityAccount> selectSseOrderSecurityAccountList(SseOrderSecurityAccount sseOrderSecurityAccount);

    /**
     * 新增加持仓统计
     *
     * @param sseOrderSecurityAccount 加持仓统计
     * @
     */
    public int insertSseOrderSecurityAccount(SseOrderSecurityAccount sseOrderSecurityAccount);

    /**
     * 修改加持仓统计
     *
     * @param sseOrderSecurityAccount 加持仓统计
     * @return 结果
     */
    public int updateSseOrderSecurityAccount(SseOrderSecurityAccount sseOrderSecurityAccount);

    /**
     * 批量删除加持仓统计
     *
     * @param pkIds 需要删除的加持仓统计主键集合
     * @return 结果
     */
    public int deleteSseOrderSecurityAccountByPkIds(Long[] pkIds);

    /**
     * 删除加持仓统计信息
     *
     * @param pkId 加持仓统计主键
     * @return 结果
     */
    public int deleteSseOrderSecurityAccountByPkId(Long pkId);

    public int batchInsertSseOrderSecurityAccount(List<SseOrderSecurityAccount> sseOrderSecurityAccountList);
}
