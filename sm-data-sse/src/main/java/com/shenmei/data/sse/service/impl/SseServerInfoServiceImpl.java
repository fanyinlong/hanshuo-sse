package com.shenmei.data.sse.service.impl;

import java.util.List;

import com.shenmei.data.common.utils.sse.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shenmei.data.sse.mapper.SseServerInfoMapper;
import com.shenmei.data.sse.domain.SseServerInfo;
import com.shenmei.data.sse.service.ISseServerInfoService;


/**
 * 主机信息Service业务层处理
 *
 * @author Song
 * @date 2025-07-29
 */
@Service
public class SseServerInfoServiceImpl implements ISseServerInfoService
{
    @Autowired
    private SseServerInfoMapper sseServerInfoMapper;
    private static String key = "1234567890123456";

    /**
     * 查询主机信息
     *
     * @param serverId 主机信息主键
     * @return 主机信息
     */
    @Override
    public SseServerInfo selectSseServerInfoByServerId(String serverId) throws Exception {
        SseServerInfo sseServerInfo = sseServerInfoMapper.selectSseServerInfoByServerId(serverId);
        sseServerInfo.setLoginName(Encrypt.mask(Encrypt.decrypt(sseServerInfo.getLoginName(), key)));
        sseServerInfo.setAuthCode(Encrypt.mask(Encrypt.decrypt(sseServerInfo.getAuthCode(), key)));
        return sseServerInfo;
    }

    /**
     * 查询主机信息列表
     *
     * @param sseServerInfo 主机信息
     * @return 主机信息
     */
    @Override
    public List<SseServerInfo> selectSseServerInfoList(SseServerInfo sseServerInfo) throws Exception {
        List<SseServerInfo> sseServerInfoList = sseServerInfoMapper.selectSseServerInfoList(sseServerInfo);
        for(SseServerInfo serverInfo : sseServerInfoList) {
            serverInfo.setLoginName(Encrypt.mask(Encrypt.decrypt(serverInfo.getLoginName(), key)));
            serverInfo.setAuthCode(Encrypt.mask(Encrypt.decrypt(serverInfo.getAuthCode(), key)));
        }
        return sseServerInfoList;

    }

    /**
     * 新增主机信息
     *
     * @param sseServerInfo 主机信息
     * @return 结果
     */
    @Override
    public int insertSseServerInfo(SseServerInfo sseServerInfo) throws Exception {
        sseServerInfo.setLoginName(Encrypt.encrypt(sseServerInfo.getLoginName(), key));
        sseServerInfo.setAuthCode(Encrypt.encrypt(sseServerInfo.getAuthCode(), key));
        return sseServerInfoMapper.insertSseServerInfo(sseServerInfo);
    }

    /**
     * 修改主机信息
     *
     * @param sseServerInfo 主机信息
     * @return 结果
     */
    @Override
    public int updateSseServerInfo(SseServerInfo sseServerInfo) throws Exception {
        sseServerInfo.setLoginName(Encrypt.encrypt(sseServerInfo.getLoginName(), key));
        sseServerInfo.setAuthCode(Encrypt.encrypt(sseServerInfo.getAuthCode(), key));
        return sseServerInfoMapper.updateSseServerInfo(sseServerInfo);
    }

    /**
     * 批量删除主机信息
     *
     * @param serverIds 需要删除的主机信息主键
     * @return 结果
     */
    @Override
    public int deleteSseServerInfoByServerIds(String[] serverIds)
    {
        return sseServerInfoMapper.deleteSseServerInfoByServerIds(serverIds);
    }

    /**
     * 删除主机信息信息
     *
     * @param serverId 主机信息主键
     * @return 结果
     */
    @Override
    public int deleteSseServerInfoByServerId(String serverId)
    {
        return sseServerInfoMapper.deleteSseServerInfoByServerId(serverId);
    }

}
