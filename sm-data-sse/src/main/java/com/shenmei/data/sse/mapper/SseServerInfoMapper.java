package com.shenmei.data.sse.mapper;

import java.util.List;
import com.shenmei.data.sse.domain.SseServerInfo;

/**
 * 主机信息Mapper接口
 *
 * @author Song
 * @date 2025-07-29
 */
public interface SseServerInfoMapper
{
    /**
     * 查询主机信息
     *
     * @param serverId 主机信息主键
     * @return 主机信息
     */
    public SseServerInfo selectSseServerInfoByServerId(String serverId);

    /**
     * 查询主机信息列表
     *
     * @param sseServerInfo 主机信息
     * @return 主机信息集合
     */
    public List<SseServerInfo> selectSseServerInfoList(SseServerInfo sseServerInfo);

    /**
     * 新增主机信息
     *
     * @param sseServerInfo 主机信息
     * @return 结果
     */
    public int insertSseServerInfo(SseServerInfo sseServerInfo);

    /**
     * 修改主机信息
     *
     * @param sseServerInfo 主机信息
     * @return 结果
     */
    public int updateSseServerInfo(SseServerInfo sseServerInfo);

    /**
     * 删除主机信息
     *
     * @param serverId 主机信息主键
     * @return 结果
     */
    public int deleteSseServerInfoByServerId(String serverId);

    /**
     * 批量删除主机信息
     *
     * @param serverIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSseServerInfoByServerIds(String[] serverIds);
}
