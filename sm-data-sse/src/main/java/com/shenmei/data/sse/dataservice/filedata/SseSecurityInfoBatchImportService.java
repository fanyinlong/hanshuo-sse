package com.shenmei.data.sse.dataservice.filedata;

import com.shenmei.data.common.config.RuoYiConfig;
import com.shenmei.data.sse.domain.SseSecurityInfo;
import com.shenmei.data.sse.service.ISseSecurityInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class SseSecurityInfoBatchImportService {
    private static final Logger log = LoggerFactory.getLogger(SseSecurityInfoBatchImportService.class);

    private static final String FILE_PATH = RuoYiConfig.getUploadPath() + "/sse/cpxx02011225.txt";

    private static final int BATCH_SIZE = 5;

    @Resource
    private ISseSecurityInfoService sseSecurityInfoService;

    /**
     * 批量导入产品信息
     */
    public void batchImportSecurityInfo() {
        sseSecurityInfoService.clearSseSecurityInfo();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(FILE_PATH), "GBK"))) {
//            // 处理文件内容
//
//        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            List<SseSecurityInfo> batchList = new ArrayList<>(BATCH_SIZE);
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                SseSecurityInfo sseSecurityInfo = parseLineToSecurityInfo(line, lineNumber);
                if (sseSecurityInfo != null) {
                    batchList.add(sseSecurityInfo);
                }

                // 每5行批量处理一次
                if (batchList.size() >= BATCH_SIZE) {
                    batchInsertSecurityInfo(batchList);
                    batchList.clear();
                }
            }

            // 处理剩余不足5行的数据
            if (!batchList.isEmpty()) {
                batchInsertSecurityInfo(batchList);
            }

            log.info("产品信息批量导入完成，共处理 {} 行数据", lineNumber);

        } catch (IOException e) {
            log.error("读取文件时发生错误: ", e);
        }
    }

    /**
     * 解析单行数据为SseSecurityInfo对象
     *
     * @param line       文件行内容
     * @param lineNumber 行号（用于日志记录）
     * @return SseSecurityInfo对象
     */
    private SseSecurityInfo parseLineToSecurityInfo(String line, int lineNumber) {
        try {
            if (line == null || line.trim().isEmpty()) {
                return null;
            }

            // 按照 | 分隔符拆分字段
            String[] fields = line.split("\\|");

            SseSecurityInfo sseSecurityInfo = new SseSecurityInfo();

            // 根据文件内容顺序设置字段值
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
                    log.warn("第{}行ParValue转换失败: {}", lineNumber, fields[10]);
                }
            }
            if (fields.length > 11 && !fields[11].trim().isEmpty()) {
                try {
                    sseSecurityInfo.setUnissuedSecurities(Long.valueOf(fields[11].trim()));
                } catch (NumberFormatException e) {
                    log.warn("第{}行UnissuedSecurities转换失败: {}", lineNumber, fields[11]);
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
                    log.warn("第{}行SecuritySet转换失败: {}", lineNumber, fields[14]);
                }
            }
            if (fields.length > 15 && !fields[15].trim().isEmpty()) {
                try {
                    sseSecurityInfo.setBuyUnit(Long.valueOf(fields[15].trim()));
                } catch (NumberFormatException e) {
                    log.warn("第{}行BuyUnit转换失败: {}", lineNumber, fields[15]);
                }
            }
            if (fields.length > 16 && !fields[16].trim().isEmpty()) {
                try {
                    sseSecurityInfo.setSellUnit(Long.valueOf(fields[16].trim()));
                } catch (NumberFormatException e) {
                    log.warn("第{}行SellUnit转换失败: {}", lineNumber, fields[16]);
                }
            }
            if (fields.length > 17 && !fields[17].trim().isEmpty()) {
                try {
                    sseSecurityInfo.setLimitOrderBottom(Long.valueOf(fields[17].trim()));
                } catch (NumberFormatException e) {
                    log.warn("第{}行LimitOrderBottom转换失败: {}", lineNumber, fields[17]);
                }
            }

            if (fields.length > 18 && !fields[18].trim().isEmpty()) {
                try {
                    sseSecurityInfo.setLimitOrderTop(Long.valueOf(fields[18].trim()));
                } catch (NumberFormatException e) {
                    log.warn("第{}行LimitOrderTop转换失败: {}", lineNumber, fields[18]);
                }
            }
            if (fields.length > 19 && !fields[19].trim().isEmpty()) {
                try {
                    sseSecurityInfo.setPriorClosingPrice(Double.valueOf(fields[19].trim()));
                } catch (NumberFormatException e) {
                    log.warn("第{}行PriorClosingPrice转换失败: {}", lineNumber, fields[19]);
                }
            }

            if (fields.length > 20 && !fields[20].trim().isEmpty()) {
                try {
                    sseSecurityInfo.setPriceTick(Double.valueOf(fields[20].trim()));
                } catch (NumberFormatException e) {
                    log.warn("第{}行PriceTick转换失败: {}", lineNumber, fields[20]);
                }
            }

            if (fields.length > 21 && !fields[21].trim().isEmpty()) {
                sseSecurityInfo.setPriceLimitType(fields[21].trim());
            }

            if (fields.length > 22 && !fields[22].trim().isEmpty()) {
                try {
                    sseSecurityInfo.setPriceUpsideLimit(Double.valueOf(fields[22].trim()));
                } catch (NumberFormatException e) {
                    log.warn("第{}行PriceUpsideLimit转换失败: {}", lineNumber, fields[22]);
                }
            }

            if (fields.length > 23 && !fields[23].trim().isEmpty()) {
                try {
                    sseSecurityInfo.setPriceDownsideLimit(Double.valueOf(fields[23].trim()));
                } catch (NumberFormatException e) {
                    log.warn("第{}行PriceDownsideLimit转换失败: {}", lineNumber, fields[23]);
                }
            }

            // 根据业务需要，可以设置默认值或进行其他处理
            sseSecurityInfo.setUsageStatus("0"); // 文件中未提供该字段，设置默认值
            return sseSecurityInfo;
        } catch (Exception e) {
            log.error("解析第{}行数据时发生错误: {}", lineNumber, line, e);
            return null;
        }
    }

    /**
     * 批量插入产品信息到数据库
     *
     * @param sseSecurityInfoList 产品信息列表
     */
    private void batchInsertSecurityInfo(List<SseSecurityInfo> sseSecurityInfoList) {
        if (sseSecurityInfoList == null || sseSecurityInfoList.isEmpty()) {
            return;
        }

        try {
            // 调用Mapper进行批量插入
            int insertedCount = sseSecurityInfoService.batchInsertSseSecurityInfo(sseSecurityInfoList);
            log.info("批量插入 {} 条产品信息，成功插入 {} 条", sseSecurityInfoList.size(), insertedCount);

            log.info("批量插入 {} 条产品信息", sseSecurityInfoList.size());

            // 为了演示，这里只是打印对象信息
            for (int i = 0; i < sseSecurityInfoList.size(); i++) {
                SseSecurityInfo info = sseSecurityInfoList.get(i);
                log.debug("第{}条数据: securityId={}, securityCname={}, securitySet={}",
                        i + 1, info.getSecurityId(), info.getSecurityCname(), info.getSecuritySet());
            }

        } catch (Exception e) {
            log.error("批量插入产品信息时发生错误: ", e);
        }
    }


    public static void main(String[] args) {
        SseSecurityInfoBatchImportService service = new SseSecurityInfoBatchImportService();
        service.batchImportSecurityInfo();
    }
}
