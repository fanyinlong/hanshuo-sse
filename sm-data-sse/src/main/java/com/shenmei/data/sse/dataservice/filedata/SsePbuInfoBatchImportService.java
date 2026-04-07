package com.shenmei.data.sse.dataservice.filedata;

import com.shenmei.data.common.config.RuoYiConfig;
import com.shenmei.data.sse.domain.SsePbuInfo;
import com.shenmei.data.sse.service.ISsePbuInfoService;
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
public class SsePbuInfoBatchImportService {

    private static final Logger log = LoggerFactory.getLogger(SsePbuInfoBatchImportService.class);

    private static final String FILE_PATH = RuoYiConfig.getUploadPath() + "/sse/pbuinfo.txt";

    private static final int BATCH_SIZE = 1000;


    @Resource
    private ISsePbuInfoService ssePbuInfoService;

    /**
     * 批量导入PBU信息
     */
    public void batchImportPbuInfo() {

        ssePbuInfoService.clearSsePbuInfo();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            List<SsePbuInfo> batchList = new ArrayList<>(BATCH_SIZE);
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                SsePbuInfo pbuInfo = parseLineToPbuInfo(line, lineNumber);
                if (pbuInfo != null) {
                    batchList.add(pbuInfo);
                }

                // 每5行批量处理一次
                if (batchList.size() >= BATCH_SIZE) {
                    batchInsertPbuInfo(batchList);
                    batchList.clear();
                }
            }

            // 处理剩余不足5行的数据
            if (!batchList.isEmpty()) {
                batchInsertPbuInfo(batchList);
            }

            log.info("PBU信息批量导入完成，共处理 {} 行数据", lineNumber);

        } catch (IOException e) {
            log.error("读取文件时发生错误: ", e);
        }
    }

    /**
     * 解析单行数据为SsePbuInfo对象
     *
     * @param line       文件行内容
     * @param lineNumber 行号（用于日志记录）
     * @return SsePbuInfo对象
     */
    private SsePbuInfo parseLineToPbuInfo(String line, int lineNumber) {
        try {
            if (line == null || line.trim().isEmpty()) {
                return null;
            }

            // 按照 | 分隔符拆分字段
            String[] fields = line.split("\\|");

            if (fields.length > 3 && !fields[3].trim().isEmpty() && "Y".equals(fields[3].trim())) {
                SsePbuInfo pbuInfo = new SsePbuInfo();
                // 根据文件内容顺序设置字段值
                if (fields.length > 0 && !fields[0].trim().isEmpty()) {
                    pbuInfo.setPbuId(fields[0].trim());
                }
                if (fields.length > 1 && !fields[1].trim().isEmpty()) {
                    pbuInfo.setMktId(fields[1].trim());
                }

                if (fields.length > 2 && !fields[2].trim().isEmpty()) {
                    pbuInfo.setPbuType(fields[2].trim());
                }

                pbuInfo.setStatus(fields[3].trim());

                if (fields.length > 4 && !fields[4].trim().isEmpty()) {
                    pbuInfo.setBuyonMarginind(fields[4].trim());
                }
                if (fields.length > 5 && !fields[5].trim().isEmpty()) {
                    pbuInfo.setSellonBorrowind(fields[5].trim());
                }
                if (fields.length > 6 && !fields[6].trim().isEmpty()) {
                    pbuInfo.setLoginPwd(fields[6].trim());
                }
                if (fields.length > 7 && !fields[7].trim().isEmpty()) {
                    pbuInfo.setInstituteId(fields[7].trim());
                }
                if (fields.length > 8 && !fields[8].trim().isEmpty()) {
                    try {
                        pbuInfo.setPartitionId(Long.valueOf(fields[8].trim()));
                    } catch (NumberFormatException e) {
                        log.warn("第{}行partitionId转换失败: {}", lineNumber, fields[8]);
                    }
                }
                if (fields.length > 9 && !fields[9].trim().isEmpty()) {
                    try {
                        pbuInfo.setFlowctrlValue(Long.valueOf(fields[9].trim()));
                    } catch (NumberFormatException e) {
                        log.warn("第{}行flowctrlValue转换失败: {}", lineNumber, fields[9]);
                    }
                }
                if (fields.length > 10 && !fields[10].trim().isEmpty()) {
                    pbuInfo.setHeadPbuid(fields[10].trim());
                }
                if (fields.length > 11 && !fields[11].trim().isEmpty()) {
                    pbuInfo.setClearMember(fields[11].trim());
                }
                if (fields.length > 12 && !fields[12].trim().isEmpty()) {
                    pbuInfo.setMembCert(fields[12].trim());
                }


                return pbuInfo;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("解析第{}行数据时发生错误: {}", lineNumber, line, e);
            return null;
        }
    }

    /**
     * 批量插入PBU信息到数据库
     *
     * @param pbuInfoList PBU信息列表
     */
    private void batchInsertPbuInfo(List<SsePbuInfo> pbuInfoList) {
        if (pbuInfoList == null || pbuInfoList.isEmpty()) {
            return;
        }

        try {
            // 调用Mapper进行批量插入
            int insertedCount = ssePbuInfoService.batchInsertSsePbuInfo(pbuInfoList);
            log.info("批量插入 {} 条PBU信息，成功插入 {} 条", pbuInfoList.size(), insertedCount);

        } catch (Exception e) {
            log.error("批量插入PBU信息时发生错误: ", e);
        }
    }

//    public static void main(String[] args) {
//        SsePbuInfoBatchImportService service = new SsePbuInfoBatchImportService();
//        service.batchImportPbuInfo();
//    }
}
