package com.shenmei.data.sse.mapper;

import com.shenmei.data.sse.domain.SseSecurityInfo;
import com.shenmei.data.sse.dto.CountCommonDo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品信息Mapper接口
 *
 * @author Song
 * @date 2025-07-26
 */
public interface SseSecurityInfoMapper
{
    /**
     * 查询产品信息
     *
     * @param pkId 产品信息主键
     * @return 产品信息
     */
    public SseSecurityInfo selectSseSecurityInfoByPkId(Long pkId);
    /**
     * 查询产品信息
     *
     * @param securityId 证券代码
     * @return 产品信息
     */
    public SseSecurityInfo selectSseSecurityInfoBySecurityId(String securityId);

    /**
     * 查询产品信息列表
     *
     * @param sseSecurityInfo 产品信息
     * @return 产品信息集合
     */
    public List<SseSecurityInfo> selectSseSecurityInfoList(SseSecurityInfo sseSecurityInfo);

    /**
     * 新增产品信息
     *
     * @param sseSecurityInfo 产品信息
     * @return 结果
     */
    public int insertSseSecurityInfo(SseSecurityInfo sseSecurityInfo);

    /**
     * 修改产品信息
     *
     * @param sseSecurityInfo 产品信息
     * @return 结果
     */
    public int updateSseSecurityInfo(SseSecurityInfo sseSecurityInfo);

    /**
     * 删除产品信息
     *
     * @param pkId 产品信息主键
     * @return 结果
     */
    public int deleteSseSecurityInfoByPkId(Long pkId);

    /**
     * 批量删除产品信息
     *
     * @param pkIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSseSecurityInfoByPkIds(Long[] pkIds);

    /**
     * 批量插入产品信息
     * @param sseSecurityInfoList 产品信息列表
     * @return 插入记录数
     */
    public int batchInsertSseSecurityInfo(List<SseSecurityInfo> sseSecurityInfoList);

    /**
     * 清空sse_security_info表所有数据
     *
     * @return 删除的记录数
     */
    int clearSseSecurityInfo();

    /**
     * 通过set查询股票数量
     *
     * @return 数量
     */
    List<CountCommonDo> securitySetCount(SseSecurityInfo sseSecurityInfo);


    /**
     * 根据条件随机获取指定数量的security_id
     *
     * @param sseSecurityInfo 查询条件
     * @param count 需要返回的数量
     * @return security_id列表
     */
    List<SseSecurityInfo> selectRandomSecurityIds(@Param("sseSecurityInfo") SseSecurityInfo sseSecurityInfo, @Param("count") int count);}
