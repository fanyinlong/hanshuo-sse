package com.shenmei.data.sse.dataservice.ruledata;

import com.shenmei.data.sse.domain.SsePbuConfig;
import com.shenmei.data.sse.service.ISseAccountInfoService;
import com.shenmei.data.sse.service.ISsePbuConfigService;
import com.shenmei.data.sse.service.ISsePbuInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SsePbuConfigInitService {
    private static final Logger log = LoggerFactory.getLogger(SsePbuConfigInitService.class);

    private static final int BATCH_SIZE = 1000;

    @Resource
    private ISsePbuConfigService ssePbuConfigService;
    @Resource
    private ISseAccountInfoService sseAccountInfoService;

    @Resource
    private ISsePbuInfoService ssePbuInfoService;

    /**
     * 初始化PBU配置数据
     * 将sse_pbu_info表中的pbu_id复制到sse_pbu_config表中
     */
    @Transactional(rollbackFor = Exception.class)
    public void initPbuConfig() {
        log.info("开始初始化PBU配置数据...");

        // 1. 清空配置表
        ssePbuConfigService.clearSsePbuConfig();
        log.info("已清空sse_pbu_config表");

        // 2. 获取所有PBU ID
        List<SsePbuConfig>  ssePbuConfigList = new ArrayList<>(BATCH_SIZE);
        List<String> pbuIds = ssePbuInfoService.selectAllSsePbuIds();
        if (CollectionUtils.isEmpty(pbuIds)) {
            log.warn("sse_pbu_info表中没有数据，跳过初始化");
            return;
        }
        log.info("从sse_pbu_info表获取到{}个PBU ID", pbuIds.size());

        //3.获取所有PBUID的账户数
        Map<String,Long> pbuIdMap = new HashMap<>(pbuIds.size());
        for (String pbuId : pbuIds) {
            pbuIdMap.put(pbuId,sseAccountInfoService.countPbuIdAccountNumber(pbuId));
        }

        // 4. 批量插入配置表
        int total = 0;
        for (int i = 0; i < pbuIds.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, pbuIds.size());
            List<String> batchIds = pbuIds.subList(i, end);

            // 创建配置对象并插入
            for (String pbuId : batchIds) {
                SsePbuConfig ssePbuConfig = new SsePbuConfig();
                ssePbuConfig.setPbuId(pbuId);
                ssePbuConfig.setIsBizpbu("0");
                ssePbuConfig.setIsUsed("0");
                ssePbuConfig.setIsLoginpbu("0");
                ssePbuConfig.setBelongtoLoginpbu("");
                ssePbuConfig.setAccountNumber(pbuIdMap.get(pbuId));
                ssePbuConfig.setAccountNumberused(0L);
                ssePbuConfigList.add(ssePbuConfig);
            }

            total += batchIds.size();
            batchInsertPbuConfig(ssePbuConfigList);
            log.info("已插入{}个PBU配置，进度: {}/{}", batchIds.size(), total, pbuIds.size());
            ssePbuConfigList.clear();
        }

        log.info("PBU配置数据初始化完成，共插入{}条记录", total);
    }


    /**
     * 批量插入PBU配置到数据库
     *
     * @param pbuConfigList PBU信息列表
     */
    private void batchInsertPbuConfig(List<SsePbuConfig> pbuConfigList) {
        if (pbuConfigList == null || pbuConfigList.isEmpty()) {
            return;
        }

        try {
            // 调用Mapper进行批量插入
            int insertedCount = ssePbuConfigService.batchInsertSsePbuConfig(pbuConfigList);
            log.info("批量插入 {} 条PBU配置，成功插入 {} 条", pbuConfigList.size(), insertedCount);

        } catch (Exception e) {
            log.error("批量插入PBU配置时发生错误: ", e);
        }
    }
}

