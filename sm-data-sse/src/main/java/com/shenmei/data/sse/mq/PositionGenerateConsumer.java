package com.shenmei.data.sse.mq;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.shenmei.data.sse.config.RabbitMQConfig;
import com.shenmei.data.sse.dataservice.ruledata.OrderSecurityAccountCalService;
import com.shenmei.data.sse.domain.SseOrderInfo;
import com.shenmei.data.sse.dto.OrderCompletedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PositionGenerateConsumer {

    private static final Logger log = LoggerFactory.getLogger(PositionGenerateConsumer.class);
    private static final Map<Long, Set<String>> ruleSecurityAccountSet = new ConcurrentHashMap<>();

    @Autowired
    private OrderSecurityAccountCalService calService;

    @RabbitListener(queues = RabbitMQConfig.POSITION_GENERATE_QUEUE, concurrency = "1")
    public void handlePositionGenerate(String messageJson) {
        try {
            if (messageJson.startsWith("[")) {
                List<SseOrderInfo> batch = JSON.parseObject(messageJson, new TypeReference<List<SseOrderInfo>>(){});
                if (batch == null || batch.isEmpty()) return;
                Long ruleId = batch.get(0).getRuleId();
                Set<String> securityAccountSet = ruleSecurityAccountSet.computeIfAbsent(ruleId, k -> ConcurrentHashMap.newKeySet());

                for (SseOrderInfo order : batch) {
                    if (order.getSecurityId() != null && order.getAccount() != null) {
                        securityAccountSet.add(order.getSecurityId() + "_" + order.getAccount());
                    }
                }
                log.debug("规则 {} 累计唯一组合数: {}", ruleId, securityAccountSet.size());
            } else {
                OrderCompletedMessage msg = JSON.parseObject(messageJson, OrderCompletedMessage.class);
                Set<String> all = ruleSecurityAccountSet.remove(msg.getRuleId());
                if (all != null && !all.isEmpty()) {
                    processPosition(msg.getRuleId(), all);
                }
            }
        } catch (Exception e) {
            log.error("加持仓处理失败", e);
            throw new RuntimeException("处理失败: " + e.getMessage(), e);
        }
    }

    private void processPosition(Long ruleId, Set<String> securityAccountSet) {
        log.info("开始加持仓统计, ruleId: {}, 唯一组合数: {}", ruleId, securityAccountSet.size());
        long start = System.currentTimeMillis();
        int result = calService.calOrderSecurityAccountFromSet(ruleId, securityAccountSet);
        log.info("加持仓完成, ruleId: {}, 记录数: {}, 耗时: {}ms", ruleId, result, System.currentTimeMillis() - start);
    }
}

