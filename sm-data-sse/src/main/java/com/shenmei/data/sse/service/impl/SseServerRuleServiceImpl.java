package com.shenmei.data.sse.service.impl;

import com.shenmei.data.common.core.redis.RedisCache;
import com.shenmei.data.common.utils.sse.Encrypt;
import com.shenmei.data.sse.dataservice.orderdata.SseOrderExportService;
import com.shenmei.data.sse.domain.*;
import com.shenmei.data.sse.mapper.SseDataRuleMapper;
import com.shenmei.data.sse.mapper.SsePbuConfigMapper;
import com.shenmei.data.sse.mapper.SseServerInfoMapper;
import com.shenmei.data.sse.mapper.SseServerRuleMapper;
import com.shenmei.data.sse.service.ISseOrderInfoService;
import com.shenmei.data.sse.service.ISseServerRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class SseServerRuleServiceImpl implements ISseServerRuleService {

    private static final Logger log = LoggerFactory.getLogger(SseServerRuleServiceImpl.class);

    @Autowired
    private SseServerInfoMapper sseServerInfoMapper;
    @Autowired
    private SseServerRuleMapper sseServerRuleMapper;

    @Autowired
    private SsePbuConfigMapper ssePbuConfigMapper;

    @Autowired
    private SseDataRuleMapper sseDataRuleMapper;

    @Autowired
    private ISseOrderInfoService orderInfoService;

    @Autowired
    private SseOrderExportService sseOrderExportService;

    @Autowired
    RedisCache redisCache;

    private String key = "1234567890123456";
    @Override
    public int insertSseServerRule(SseServerRule sseServerRule) throws Exception {
        SseServerInfo sseServerInfo = sseServerInfoMapper.selectSseServerInfoByServerId(sseServerRule.getServerId());
        if(sseServerInfo == null){
            throw new Exception("该主机不存在");
        }else {
            SseServerRule sseServerRule1 = sseServerRuleMapper.selectSseServerRuleByServerIdAndRuleid(sseServerRule);
            //一个主机在一个规则id只能有一个规则
            if(sseServerRule1 != null){
                throw new Exception("该主机在这个规则id上已存在规则");
            }else {
                // 同一个规则id下的pbu总数不能超过登录pbu
                SseDataRule sseDataRule = sseDataRuleMapper.selectSseDataRuleByPkId(sseServerRule.getRuleId());
                int pbuCountSum = sseServerRule.getPbuCount();
                //查询该规则id下的pbu总数
                List<SseServerRule> sseServerRuleList = sseServerRuleMapper.selectSseServerConfigByRuleId(sseServerRule.getRuleId());
                if(!sseServerRuleList.isEmpty()){
                    for (SseServerRule serverRule : sseServerRuleList) {
                        pbuCountSum =  pbuCountSum + serverRule.getPbuCount();
                    }
                }
                if(pbuCountSum > sseDataRule.getLoginPbu()){
                    throw new Exception("该规则id下pbu总数不能超过登录pbu个数");
                }else {
                    sseServerRule.setServerName(sseServerInfo.getServerName());
                    return sseServerRuleMapper.insertSseServerRule(sseServerRule);
                }
            }
        }
    }

    @Override
    public List<SseServerRule> selectSseServerInfoList(SseServerRule sseServerRule) throws Exception {
        List<SseServerRule> sseServerRuleList = sseServerRuleMapper.selectSseServerRuleList(sseServerRule);
        return sseServerRuleList;
    }

    /**
     * 批量删除主机规则信息
     *
     * @param ids 需要删除的主机信息主键
     * @return 结果
     */
    @Override
    public int deleteSseServerRuleByServerIds(String[] ids)
    {
        return sseServerRuleMapper.deleteSseServerRuleByServerIds(ids);
    }

    @Override
    public SseServerRule selectSseServerRuleById(String id) {
        return sseServerRuleMapper.selectSseServerRuleById(id);
    }

    @Override
    public int updateSseServerRule(SseServerRule sseServerRule) throws Exception {
        SseServerRule sseServerRule1 = sseServerRuleMapper.selectSseServerRuleByServerIdAndRuleid(sseServerRule);
        if (sseServerRule1 == null){

            // 同一个规则id下的pbu总数不能超过登录pbu
            SseDataRule sseDataRule = sseDataRuleMapper.selectSseDataRuleByPkId(sseServerRule.getRuleId());
            int pbuCountSum = sseServerRule.getPbuCount();
            //查询该规则id下的pbu总数
            List<SseServerRule> sseServerRuleList = sseServerRuleMapper.selectSseServerConfigByRuleId(sseServerRule.getRuleId());
            if(!sseServerRuleList.isEmpty()){
                for (SseServerRule serverRule : sseServerRuleList) {
                    pbuCountSum =  pbuCountSum + serverRule.getPbuCount();
                }
            }
            if(pbuCountSum > sseDataRule.getLoginPbu()){
                throw new Exception("该规则id下pbu总数不能超过登录pbu个数");
            }else {
                return sseServerRuleMapper.updateSseServerRule(sseServerRule);
            }

        }else{
            SseServerRule sseServerRule2 = sseServerRuleMapper.selectSseServerRuleById(sseServerRule.getId());
            if(sseServerRule.getRuleId().equals(sseServerRule2.getRuleId())){

                // 同一个规则id下的pbu总数不能超过登录pbu
                SseDataRule sseDataRule = sseDataRuleMapper.selectSseDataRuleByPkId(sseServerRule.getRuleId());
                int pbuCountSum = sseServerRule.getPbuCount();
                //查询该规则id下的pbu总数
                List<SseServerRule> sseServerRuleList = sseServerRuleMapper.selectSseServerConfigByRuleId(sseServerRule.getRuleId());
                if(!sseServerRuleList.isEmpty()){
                    for (SseServerRule serverRule : sseServerRuleList) {
                        pbuCountSum =  pbuCountSum + serverRule.getPbuCount();
                    }
                    pbuCountSum = pbuCountSum - sseServerRule2.getPbuCount();
                }
                if(pbuCountSum > sseDataRule.getLoginPbu()){
                    throw new Exception("该规则id下pbu总数不能超过登录pbu个数");
                }else {
                    return sseServerRuleMapper.updateSseServerRule(sseServerRule);
                }

            }else {
                throw new Exception("该主机在这个规则id上已存在规则");

            }
        }
    }

    /**
     * 数据分发执行
     *
     * @param  sseServerDistributionRecord
     * @return 结果
     */
    @Override
    public int orderDataDistribution(SseServerDistributionRecord sseServerDistributionRecord) {

        List<SseServerRule> sseServerConfigList = sseServerRuleMapper.selectSseServerConfigByRuleId(sseServerDistributionRecord.getRuleId());
        SsePbuConfig ssePbuConfig = new SsePbuConfig();
        ssePbuConfig.setRuleId(sseServerDistributionRecord.getRuleId());
        ssePbuConfig.setIsLoginpbu("1");
        List<SsePbuConfig> ssePbuConfigs = ssePbuConfigMapper.selectSsePbuConfigList(ssePbuConfig);
        int ssePbuConfigsCount = 0 ;

        // 2. 创建线程池（根据CPU核心数优化）
        int threadPoolSize = Runtime.getRuntime().availableProcessors() * 2;
        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);
        List<Future<Void>> futures = new ArrayList<>();
        for (SseServerRule sseServerConfig : sseServerConfigList) {
            SseServerInfo sseServerInfo = sseServerInfoMapper.selectSseServerInfoByServerId(sseServerConfig.getServerId().toString());

            if (sseServerConfig == null ||  sseServerInfo == null) {
                log.error("sseServerConfig 或者 sseServerInfo 为空，规则ID为：" + sseServerDistributionRecord.getRuleId());
                continue;
            }
            for (int i = 0 ; i < Integer.valueOf(sseServerConfig.getPbuCount()) ; i ++) {
                String orderTable = sseServerInfo.getPlatformId() + "_" + sseServerInfo.getEnvId() + "_" + ssePbuConfigs.get(ssePbuConfigsCount).getPbuId();
                ssePbuConfigsCount++;
                try {
                    processOrderTable(orderTable, sseServerDistributionRecord.getRuleId(), sseServerInfo, sseServerDistributionRecord.getPkId());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                Callable<Void> task = () -> processOrderTable(orderTable, sseServerDistributionRecord.getRuleId(), sseServerInfo, sseServerDistributionRecord.getPkId());
                futures.add(executorService.submit(task));
            }
        }

        // 4. 等待所有任务完成
        for (Future<Void> future : futures) {
            try {
                future.get(); // 抛出异常时会在此处阻塞
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        return 0;
    }

    /**
     * 处理单个orderTable的私有方法（线程安全）
     */
    private Void processOrderTable(String orderTable, Long ruleId, SseServerInfo sseServerInfo, Long distributionRecordId) throws IOException, SQLException {
        // 查询该orderTable下的所有订单数据

        
        SseOrderInfo queryInfo = new SseOrderInfo();
        queryInfo.setRuleId(ruleId);
        queryInfo.setOrderTable(orderTable);

        List<SseOrderInfo> orderList = new ArrayList<>();
        int pageSize = 10000; // 增大页大小减少网络请求次数
        int offset = 0;
        String url = "jdbc:sqlserver://".concat(sseServerInfo.getIpAddress()).concat(":").concat(sseServerInfo.getPort())
                .concat(";DatabaseName=").concat(sseServerInfo.getInstanceName()).concat(";trustServerCertificate=true");
        String username = null;
        String password = null;
        try {
            username = Encrypt.decrypt(sseServerInfo.getLoginName(), key);
            password = Encrypt.decrypt(sseServerInfo.getAuthCode(), key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 连接对象
        Connection connection = null;
        try {
            // 加载SQL Server驱动
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // 建立连接
            connection = DriverManager.getConnection(url, username, password);

            // 如果成功连接，输出连接信息
            if (connection != null) {
                log.debug("成功连接到数据库！");
            }
            else {
                log.info("数据库连接失败！");
            }
            String orderTableName =  orderTable + "_OrderRequest";
            String dropSql = "drop table " + orderTableName;
            Statement dropStmt = connection.createStatement();
            try {
                dropStmt.execute(dropSql);
            }
            catch (SQLException e) {
                log.info(e.getMessage());
            }
            String createSql = "CREATE TABLE [" + orderTableName + "](\n" +
                    "\t[MsgType] [char](2) NOT NULL,\n" +
                    "\t[BizID] [int] NULL,\n" +
                    "\t[BizPbu] [char](8) NULL,\n" +
                    "\t[ClOrdID] [char](10) NULL,\n" +
                    "\t[SecurityID] [char](12) NULL,\n" +
                    "\t[Account] [char](13) NULL,\n" +
                    "\t[OwnerType] [int] NULL,\n" +
                    "\t[Side] [char](1) NULL,\n" +
                    "\t[Price] [numeric](18, 4) NULL,\n" +
                    "\t[OrderQty] [int] NULL,\n" +
                    "\t[OrdType] [char](1) NULL,\n" +
                    "\t[TimeInForce] [char](1) NULL,\n" +
                    "\t[TransactTime] [varchar](50) NULL,\n" +
                    "\t[CreditTag] [char](2) NULL,\n" +
                    "\t[ClearingFirm] [char](8) NULL,\n" +
                    "\t[BranchID] [char](8) NULL,\n" +
                    "\t[OrigClOrdID] [char](10) NULL,\n" +
                    "\t[UserInfo] [char](32) NULL,\n" +
                    "\t[ExtendFields] [nchar](10) NULL,\n" +
                    "\t[ErrorCode] [int] NULL,\n" +
                    "\t[Status] [int] NULL\n" +
                    ") ON [PRIMARY]";
            Statement createStmt = connection.createStatement();
            createStmt.execute(createSql);
            dropStmt.close();
            createStmt.close();

            while (true) {
                List<SseOrderInfo> batchList = orderInfoService.selectSseOrderInfoListByPage(queryInfo, offset, pageSize);
                if (batchList.isEmpty()) {
                    break;
                }
                String insertBatchSql = sseOrderExportService.generateBatchInsertSql(orderTable,batchList);

                Statement insertStmt = connection.createStatement();
                insertStmt.execute(insertBatchSql);
                insertStmt.close();
                if (batchList.size() < pageSize) {
                    break;
                }
                offset += pageSize;

                // 更新Redis缓存
                if (distributionRecordId != null) {
                    try {
                        String cacheKey = "sseServerDistribution_generatedCount_" + distributionRecordId;
                        redisCache.increment(cacheKey, batchList.size());
                        redisCache.expire(cacheKey, 48, TimeUnit.HOURS);
                    } catch (Exception e) {
                        log.error("Redis缓存更新失败", e);
                    }
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            // 如果出现异常，打印错误信息
            log.error("数据库连接失败: " + e.getMessage());
        } finally {
            // 关闭连接
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                log.error("关闭连接失败: " + e.getMessage());
            }
        }

        return null;
    }




}
