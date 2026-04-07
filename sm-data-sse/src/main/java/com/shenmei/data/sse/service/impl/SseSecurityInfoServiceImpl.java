package com.shenmei.data.sse.service.impl;

import com.shenmei.data.common.utils.DateUtils;
import com.shenmei.data.sse.dataservice.filedata.SseSecurityInfoBatchImportService;
import com.shenmei.data.sse.domain.SseSecurityInfo;
import com.shenmei.data.sse.dto.CountCommonDo;
import com.shenmei.data.sse.mapper.SseSecurityInfoMapper;
import com.shenmei.data.sse.service.ISseSecurityInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 产品信息Service业务层处理
 *
 * @author Song
 * @date 2025-07-26
 */
@Service
public class SseSecurityInfoServiceImpl implements ISseSecurityInfoService {
    @Autowired
    private SseSecurityInfoMapper sseSecurityInfoMapper;

    private static final int BATCH_SIZE = 5;

    private static final Logger log = LoggerFactory.getLogger(SseSecurityInfoBatchImportService.class);




    @Resource
    SseSecurityInfoBatchImportService sseSecurityInfoBatchImportService;

    /**
     * 查询产品信息
     *
     * @param pkId 产品信息主键
     * @return 产品信息
     */
    @Override
    public SseSecurityInfo selectSseSecurityInfoByPkId(Long pkId) {
        return sseSecurityInfoMapper.selectSseSecurityInfoByPkId(pkId);
    }

    /**
     * 查询产品信息列表
     *
     * @param sseSecurityInfo 产品信息
     * @return 产品信息
     */
    @Override
    public List<SseSecurityInfo> selectSseSecurityInfoList(SseSecurityInfo sseSecurityInfo) {
        return sseSecurityInfoMapper.selectSseSecurityInfoList(sseSecurityInfo);
    }

    /**
     * 新增产品信息
     *
     * @param sseSecurityInfo 产品信息
     * @return 结果
     */
    @Override
    public int insertSseSecurityInfo(SseSecurityInfo sseSecurityInfo) {
        return sseSecurityInfoMapper.insertSseSecurityInfo(sseSecurityInfo);
    }

    /**
     * 修改产品信息
     *
     * @param sseSecurityInfo 产品信息
     * @return 结果
     */
    @Override
    public int updateSseSecurityInfo(SseSecurityInfo sseSecurityInfo) {
        sseSecurityInfo.setUpdateTime(DateUtils.getNowDate());
        return sseSecurityInfoMapper.updateSseSecurityInfo(sseSecurityInfo);
    }

    /**
     * 批量删除产品信息
     *
     * @param pkIds 需要删除的产品信息主键
     * @return 结果
     */
    @Override
    public int deleteSseSecurityInfoByPkIds(Long[] pkIds) {
        return sseSecurityInfoMapper.deleteSseSecurityInfoByPkIds(pkIds);
    }

    /**
     * 删除产品信息信息
     *
     * @param pkId 产品信息主键
     * @return 结果
     */
    @Override
    public int deleteSseSecurityInfoByPkId(Long pkId) {
        return sseSecurityInfoMapper.deleteSseSecurityInfoByPkId(pkId);
    }

    @Override
    public int batchInsertSseSecurityInfo(List<SseSecurityInfo> sseSecurityInfoList) {
        return sseSecurityInfoMapper.batchInsertSseSecurityInfo(sseSecurityInfoList);
    }


    @Override
    public void init() {
        sseSecurityInfoBatchImportService.batchImportSecurityInfo();
    }

    @Override
    public int clearSseSecurityInfo() {
        return sseSecurityInfoMapper.clearSseSecurityInfo();
    }

    @Override
    public List<CountCommonDo> SecuritySetCount(SseSecurityInfo sseSecurityInfo) {
        return sseSecurityInfoMapper.securitySetCount(sseSecurityInfo);
    }

    @Override
    public List<SseSecurityInfo> selectRandomSecurityIds(SseSecurityInfo sseSecurityInfo, int count) {
        return sseSecurityInfoMapper.selectRandomSecurityIds(sseSecurityInfo, count);
    }

    @Override
    public int importSecurityInfoFromFile(MultipartFile file) throws Exception {
        // 清空现有数据
        this.clearSseSecurityInfo();

        // 获取文件输入流
        try (InputStream inputStream = file.getInputStream();
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(inputStream, "GBK"))) {

            String line;
            List<SseSecurityInfo> batchList = new ArrayList<>(BATCH_SIZE);
            int lineNumber = 0;
            int totalCount = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                SseSecurityInfo sseSecurityInfo = parseLineToSecurityInfo(line, lineNumber);
                if (sseSecurityInfo != null) {
                    batchList.add(sseSecurityInfo);
                }

                // 每 5 行批量处理一次
                if (batchList.size() >= BATCH_SIZE) {
                    totalCount += batchInsertSseSecurityInfo(batchList);
                    batchList.clear();
                }
            }

            // 处理剩余不足 5 行的数据
            if (!batchList.isEmpty()) {
                totalCount += batchInsertSseSecurityInfo(batchList);
            }

            log.info("产品信息从文件导入完成，共处理 {} 行数据，成功导入 {} 条", lineNumber, totalCount);
            return totalCount;
        } catch (IOException e) {
            log.error("读取文件时发生错误：", e);
            throw new Exception("文件读取失败：" + e.getMessage());
        }
    }

    /**
     * 解析单行数据为 SseSecurityInfo 对象
     */
    private SseSecurityInfo parseLineToSecurityInfo(String line, int lineNumber) {
        // 这里复用你之前在 SseSecurityInfoBatchImportService 中的解析逻辑
        try {
            if (line == null || line.trim().isEmpty()) {
                return null;
            }

            String[] fields = line.split("\\|");
            SseSecurityInfo sseSecurityInfo = new SseSecurityInfo();

            if (fields.length > 0 && !fields[0].trim().isEmpty()) {
                sseSecurityInfo.setSecurityId(fields[0].trim());
            }
            if (fields.length > 1 && !fields[1].trim().isEmpty()) {
                sseSecurityInfo.setIsinCode(fields[1].trim());
            }
            if (fields.length > 2 && !fields[2].trim().isEmpty()) {
                sseSecurityInfo.setRecordTime(fields[2].trim());
            }
            if (fields.length > 3 && !fields[3].trim().isEmpty()) {
                sseSecurityInfo.setSecurityCname(fields[3].trim());
            }
            if (fields.length > 4 && !fields[4].trim().isEmpty()) {
                sseSecurityInfo.setSecurityName(fields[4].trim());
            }
            if (fields.length > 5 && !fields[5].trim().isEmpty()) {
                sseSecurityInfo.setOriSecurityId(fields[5].trim());
            }
            if (fields.length > 6 && !fields[6].trim().isEmpty()) {
                sseSecurityInfo.setSecurityMarket(fields[6].trim());
            }
            if (fields.length > 7 && !fields[7].trim().isEmpty()) {
                sseSecurityInfo.setSecurityType(fields[7].trim());
            }
            if (fields.length > 8 && !fields[8].trim().isEmpty()) {
                sseSecurityInfo.setSecuritySubType(fields[8].trim());
            }
            if (fields.length > 9 && !fields[9].trim().isEmpty()) {
                sseSecurityInfo.setCurrencyType(fields[9].trim());
            }
            if (fields.length > 10 && !fields[10].trim().isEmpty()) {
                try {
                    sseSecurityInfo.setParValue(Double.valueOf(fields[10].trim()));
                } catch (NumberFormatException e) {
                    log.warn("第{}行 ParValue 转换失败：{}", lineNumber, fields[10]);
                }
            }
            if (fields.length > 11 && !fields[11].trim().isEmpty()) {
                try {
                    sseSecurityInfo.setUnissuedSecurities(Long.valueOf(fields[11].trim()));
                } catch (NumberFormatException e) {
                    log.warn("第{}行 UnissuedSecurities 转换失败：{}", lineNumber, fields[11]);
                }
            }
            if (fields.length > 12 && !fields[12].trim().isEmpty()) {
                sseSecurityInfo.setLastTradeDate(fields[10].trim());
            }
            if (fields.length > 13 && !fields[13].trim().isEmpty()) {
                sseSecurityInfo.setListingDate(fields[11].trim());
            }
            if (fields.length > 14 && !fields[14].trim().isEmpty()) {
                try {
                    sseSecurityInfo.setSecuritySet(Long.valueOf(fields[14].trim()));
                } catch (NumberFormatException e) {
                    log.warn("第{}行 SecuritySet 转换失败：{}", lineNumber, fields[14]);
                }
            }
            if (fields.length > 15 && !fields[15].trim().isEmpty()) {
                try {
                    sseSecurityInfo.setBuyUnit(Long.valueOf(fields[15].trim()));
                } catch (NumberFormatException e) {
                    log.warn("第{}行 BuyUnit 转换失败：{}", lineNumber, fields[15]);
                }
            }
            if (fields.length > 16 && !fields[16].trim().isEmpty()) {
                try {
                    sseSecurityInfo.setSellUnit(Long.valueOf(fields[16].trim()));
                } catch (NumberFormatException e) {
                    log.warn("第{}行 SellUnit 转换失败：{}", lineNumber, fields[16]);
                }
            }
            if (fields.length > 17 && !fields[17].trim().isEmpty()) {
                try {
                    sseSecurityInfo.setLimitOrderBottom(Long.valueOf(fields[17].trim()));
                } catch (NumberFormatException e) {
                    log.warn("第{}行 LimitOrderBottom 转换失败：{}", lineNumber, fields[17]);
                }
            }
            if (fields.length > 18 && !fields[18].trim().isEmpty()) {
                try {
                    sseSecurityInfo.setLimitOrderTop(Long.valueOf(fields[18].trim()));
                } catch (NumberFormatException e) {
                    log.warn("第{}行 LimitOrderTop 转换失败：{}", lineNumber, fields[18]);
                }
            }
            if (fields.length > 19 && !fields[19].trim().isEmpty()) {
                try {
                    sseSecurityInfo.setPriorClosingPrice(Double.valueOf(fields[19].trim()));
                } catch (NumberFormatException e) {
                    log.warn("第{}行 PriorClosingPrice 转换失败：{}", lineNumber, fields[19]);
                }
            }
            if (fields.length > 20 && !fields[20].trim().isEmpty()) {
                try {
                    sseSecurityInfo.setPriceTick(Double.valueOf(fields[20].trim()));
                } catch (NumberFormatException e) {
                    log.warn("第{}行 PriceTick 转换失败：{}", lineNumber, fields[20]);
                }
            }
            if (fields.length > 21 && !fields[21].trim().isEmpty()) {
                sseSecurityInfo.setPriceLimitType(fields[21].trim());
            }
            if (fields.length > 22 && !fields[22].trim().isEmpty()) {
                try {
                    sseSecurityInfo.setPriceUpsideLimit(Double.valueOf(fields[22].trim()));
                } catch (NumberFormatException e) {
                    log.warn("第{}行 PriceUpsideLimit 转换失败：{}", lineNumber, fields[22]);
                }
            }
            if (fields.length > 23 && !fields[23].trim().isEmpty()) {
                try {
                    sseSecurityInfo.setPriceDownsideLimit(Double.valueOf(fields[23].trim()));
                } catch (NumberFormatException e) {
                    log.warn("第{}行 PriceDownsideLimit 转换失败：{}", lineNumber, fields[23]);
                }
            }

            sseSecurityInfo.setUsageStatus("0");
            return sseSecurityInfo;
        } catch (Exception e) {
            log.error("解析第{}行数据时发生错误：{}", lineNumber, line, e);
            return null;
        }
    }


}

