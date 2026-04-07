package com.shenmei.data.sse.dataservice.filedata;

import com.shenmei.data.common.config.RuoYiConfig;
import com.shenmei.data.sse.domain.SseAccountInfo;
import com.shenmei.data.sse.service.ISseAccountInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SseAccountInfoBatchImportService {

    private static final Logger log = LoggerFactory.getLogger(SseAccountInfoBatchImportService.class);

    private static final String FILE_PATH = RuoYiConfig.getUploadPath() + "/sse/invacc.txt";

    private static final int BATCH_SIZE = 1000;

    @Resource
    private ISseAccountInfoService sseAccountInfoService;

    /**
     * 批量导入Account信息
     */
    public void batchImportAccountInfo() {

        sseAccountInfoService.clearSseAccountInfo();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            List<SseAccountInfo> batchList = new ArrayList<>(BATCH_SIZE);
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                SseAccountInfo AccountInfo = parseLineToAccountInfo(line, lineNumber);
                if (AccountInfo != null) {
                    batchList.add(AccountInfo);
                }

                // 每1000行批量处理一次
                if (batchList.size() >= BATCH_SIZE) {
                    batchInsertAccountInfo(batchList);
                    batchList.clear();
                }
            }

            // 处理剩余不足1000行的数据
            if (!batchList.isEmpty()) {
                batchInsertAccountInfo(batchList);
            }

            log.info("Account信息批量导入完成，共处理 {} 行数据", lineNumber);

        } catch (IOException e) {
            log.error("读取文件时发生错误: ", e);
        }
    }

    /**
     * 解析单行数据为SseAccountInfo对象
     *
     * @param line       文件行内容
     * @param lineNumber 行号（用于日志记录）
     * @return SseAccountInfo对象
     */
    private SseAccountInfo parseLineToAccountInfo(String line, int lineNumber) {
        try {
            if (line == null || line.trim().isEmpty()) {
                return null;
            }

            // 按照 | 分隔符拆分字段
            String[] fields = line.split("\\|");

            SseAccountInfo AccountInfo = new SseAccountInfo();
            // 根据文件内容顺序设置字段值
            if (fields.length > 0 && !fields[0].trim().isEmpty()) {
                AccountInfo.setAccountId(fields[0].trim());
            }
            if (fields.length > 1 && !fields[1].trim().isEmpty()) {
                AccountInfo.setAccountSubId(fields[1].trim());
            }

            if (fields.length > 2 && !fields[2].trim().isEmpty()) {
                AccountInfo.setPbuId(fields[2].trim());
            }

            if (fields.length > 3 && !fields[3].trim().isEmpty()) {
                AccountInfo.setAccountType(fields[3].trim());
            } else {
                String type = fields[0].trim();
                // 取第一个字符
                if (!type.isEmpty()) {
                    char firstChar = type.charAt(0);
                    AccountInfo.setAccountType(String.valueOf(firstChar));
                } else {
                    AccountInfo.setAccountType("");
                }
            }
            if (fields.length > 4 && !fields[4].trim().isEmpty()) {
                AccountInfo.setTradeType(fields[4].trim());
            }
            if (fields.length > 5 && !fields[5].trim().isEmpty()) {
                AccountInfo.setCancelFlag(fields[5].trim());
            }
            if (fields.length > 6 && !fields[6].trim().isEmpty()) {
                AccountInfo.setStatusFlag(fields[6].trim());
            }

            return AccountInfo;
        } catch (
                Exception e) {
            log.error("解析第{}行数据时发生错误: {}", lineNumber, line, e);
            return null;
        }
    }

    /**
     * 批量插入Account信息到数据库
     *
     * @param AccountInfoList Account信息列表
     */
    private void batchInsertAccountInfo(List<SseAccountInfo> AccountInfoList) {
        if (AccountInfoList == null || AccountInfoList.isEmpty()) {
            return;
        }

        try {
            // 调用Mapper进行批量插入
            int insertedCount = sseAccountInfoService.batchInsertSseAccountInfo(AccountInfoList);
            log.info("批量插入 {} 条Account信息，成功插入 {} 条", AccountInfoList.size(), insertedCount);

        } catch (Exception e) {
            log.error("批量插入Account信息时发生错误: ", e);
        }
    }

    public static void main(String[] args) {
        SseAccountInfoBatchImportService service = new SseAccountInfoBatchImportService();
        service.batchImportAccountInfo();
    }
}

