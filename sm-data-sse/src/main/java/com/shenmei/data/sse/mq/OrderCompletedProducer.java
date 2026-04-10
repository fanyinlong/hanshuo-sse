
package com.shenmei.data.sse.mq;

import com.alibaba.fastjson2.JSON;
import com.shenmei.data.sse.config.RabbitMQConfig;
import com.shenmei.data.sse.dto.OrderCompletedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class OrderCompletedProducer {

    private static final Logger log = LoggerFactory.getLogger(OrderCompletedProducer.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendOrderCompletedMessage(OrderCompletedMessage message) {
        try {
            String messageJson = JSON.toJSONString(message);

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDER_COMPLETED_EXCHANGE,
                    RabbitMQConfig.MYSQL_INSERT_ROUTING_KEY,
                    messageJson
            );
            log.info("发送MySQL插入消息成功, ruleId: {}", message.getRuleId());

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDER_COMPLETED_EXCHANGE,
                    RabbitMQConfig.SQLSERVER_DISTRIBUTION_ROUTING_KEY,
                    messageJson
            );
            log.info("发送SQL Server下发消息成功, ruleId: {}", message.getRuleId());

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDER_COMPLETED_EXCHANGE,
                    RabbitMQConfig.POSITION_GENERATE_ROUTING_KEY,
                    messageJson
            );
            log.info("发送加持仓生成消息成功, ruleId: {}", message.getRuleId());

        } catch (Exception e) {
            log.error("发送订单完成消息失败, ruleId: {}", message.getRuleId(), e);
            throw new RuntimeException("发送消息失败", e);
        }
    }
}