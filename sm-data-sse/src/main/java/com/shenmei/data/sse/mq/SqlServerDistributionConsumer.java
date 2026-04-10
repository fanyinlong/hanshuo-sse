package com.shenmei.data.sse.mq;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.shenmei.data.sse.config.RabbitMQConfig;
import com.shenmei.data.sse.domain.SseOrderInfo;
import com.shenmei.data.sse.domain.SseServerDistributionRecord;
import com.shenmei.data.sse.dto.OrderCompletedMessage;
import com.shenmei.data.sse.service.ISseServerRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class SqlServerDistributionConsumer {

    private static final Logger log = LoggerFactory.getLogger(SqlServerDistributionConsumer.class);


    @Autowired
    private ISseServerRuleService sseServerRuleService;

    @RabbitListener(queues = RabbitMQConfig.SQLSERVER_DISTRIBUTION_QUEUE, concurrency = "3-5")
    public void handleSqlServerDistribution(String messageJson) {
        try {
            if (!messageJson.startsWith("[")) return;

            List<SseOrderInfo> batch = JSON.parseObject(messageJson, new TypeReference<List<SseOrderInfo>>(){});
            if (batch == null || batch.isEmpty()) return;

            Long ruleId = batch.get(0).getRuleId();
            processBatch(ruleId, batch);
        } catch (Exception e) {
            log.error("SQLServer处理失败", e);
            throw new RuntimeException("处理失败: " + e.getMessage(), e);
        }
    }

    private void processBatch(Long ruleId, List<SseOrderInfo> orders) {
        log.info("开始SQLServer下发, ruleId: {}, 订单数: {}", ruleId, orders.size());
        long start = System.currentTimeMillis();

        SseServerDistributionRecord record = new SseServerDistributionRecord();
        record.setRuleId(ruleId);
        record.setDistributionStatus("0");
        record.setTotalCount((long) orders.size());

        sseServerRuleService.orderDataDistributionWithOrders(record, orders);
        log.info("SQLServer下发完成, ruleId: {}, 耗时: {}ms", ruleId, System.currentTimeMillis() - start);
    }
}