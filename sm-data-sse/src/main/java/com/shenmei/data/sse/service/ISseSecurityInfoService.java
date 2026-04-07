package com.shenmei.data.sse.service;

import com.shenmei.data.sse.domain.SseSecurityInfo;
import com.shenmei.data.sse.dto.CountCommonDo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 产品信息Service接口
 *
 * @author Song
 * @date 2025-07-26
 */
public interface ISseSecurityInfoService {
    /**
     * 查询产品信息
     *
     * @param pkId 产品信息主键
     * @return 产品信息
     */
    public SseSecurityInfo selectSseSecurityInfoByPkId(Long pkId);

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
     * 批量删除产品信息
     *
     * @param pkIds 需要删除的产品信息主键集合
     * @return 结果
     */
    public int deleteSseSecurityInfoByPkIds(Long[] pkIds);

    /**
     * 删除产品信息信息
     *
     * @param pkId 产品信息主键
     * @return 结果
     */
    public int deleteSseSecurityInfoByPkId(Long pkId);

    /**
     * 批量插入Security信息
     *
     * @param sseSecurityInfoList 产品信息列表
     * @return 插入记录数
     */
    int batchInsertSseSecurityInfo(List<SseSecurityInfo> sseSecurityInfoList);

    /**
     * 初始化Security信息
     */
    void init();

    /**
     * 清空Security信息
     */
    int clearSseSecurityInfo();


    /**
     * 通过set查询股票数量
     *
     * @return 数量
     */
    List<CountCommonDo> SecuritySetCount(SseSecurityInfo sseSecurityInfo);

    /**
     * 根据条件随机获取指定数量的security_id
     *
     * @param sseSecurityInfo 查询条件
     * @param count           需要返回的数量
     * @return security_id列表
     */
    List<SseSecurityInfo> selectRandomSecurityIds(SseSecurityInfo sseSecurityInfo, int count);

    /**
     * 从文件导入产品信息
     *
     * @param file 上传的文件
     * @return 导入的数据条数
     */
    public int importSecurityInfoFromFile(MultipartFile file) throws Exception;

}
