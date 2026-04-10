
package com.shenmei.data.sse.mq;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.shenmei.data.sse.config.RabbitMQConfig;
import com.shenmei.data.sse.domain.SseOrderInfo;
import com.shenmei.data.sse.service.ISseOrderInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class MysqlInsertConsumer {

    private static final Logger log = LoggerFactory.getLogger(MysqlInsertConsumer.class);
    private static final AtomicLong totalInsertedCount = new AtomicLong(0);
    private static final int MAX_BATCH_SIZE = 5000;

    @Autowired
    private ISseOrderInfoService orderInfoService;

    @RabbitListener(queues = RabbitMQConfig.MYSQL_INSERT_QUEUE, concurrency = "3-5")
    public void handleMysqlInsert(String messageJson) {
        try {
            if (!messageJson.startsWith("[")) return;
            
            List<SseOrderInfo> orderList = JSON.parseObject(messageJson, new TypeReference<List<SseOrderInfo>>(){});
            if (orderList == null || orderList.isEmpty()) return;
            
            if (orderList.size() > MAX_BATCH_SIZE) {
                log.warn("批次过大({}条),将分批处理", orderList.size());
                processInBatches(orderList);
            } else {
                processBatch(orderList);
            }
        } catch (Exception e) {
            log.error("MySQL插入失败, 消息长度: {}", messageJson.length(), e);
            throw new RuntimeException("处理失败: " + e.getMessage(), e);
        }
    }
    
    private void processInBatches(List<SseOrderInfo> orderList) {
        for (int i = 0; i < orderList.size(); i += MAX_BATCH_SIZE) {
            int end = Math.min(i + MAX_BATCH_SIZE, orderList.size());
            List<SseOrderInfo> subList = orderList.subList(i, end);
            try {
                processBatch(subList);
            } catch (Exception e) {
                log.error("批次处理失败, 起始索引: {}, 批次大小: {}", i, subList.size(), e);
                throw e;
            }
        }
    }
    
    private void processBatch(List<SseOrderInfo> orderList) {
        long startTime = System.currentTimeMillis();
        Long ruleId = orderList.get(0).getRuleId();
        orderInfoService.batchInsertSseOrderInfo(orderList);
        long elapsed = System.currentTimeMillis() - startTime;
        long totalCount = totalInsertedCount.addAndGet(orderList.size());
        
        log.info("MySQL插入完成, ruleId: {}, 批次: {}, 累计: {}, 耗时: {}ms, 速度: {}条/秒", 
                ruleId, orderList.size(), totalCount, elapsed, 
                orderList.size() * 1000 / Math.max(elapsed, 1));
    }
    
    public static long getTotalInsertedCount() {
        return totalInsertedCount.get();
    }
    
    public static void resetCounter() {
        totalInsertedCount.set(0);
    }
}
