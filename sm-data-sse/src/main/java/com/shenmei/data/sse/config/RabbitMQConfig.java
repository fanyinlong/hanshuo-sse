
package com.shenmei.data.sse.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_COMPLETED_EXCHANGE = "order.completed.exchange";

    public static final String MYSQL_INSERT_QUEUE = "mysql.insert.queue";
    public static final String SQLSERVER_DISTRIBUTION_QUEUE = "sqlserver.distribution.queue";
    public static final String POSITION_GENERATE_QUEUE = "position.generate.queue";

    public static final String MYSQL_INSERT_ROUTING_KEY = "mysql.insert";
    public static final String SQLSERVER_DISTRIBUTION_ROUTING_KEY = "sqlserver.distribution";
    public static final String POSITION_GENERATE_ROUTING_KEY = "position.generate";

    @Bean
    public FanoutExchange orderCompletedExchange() {
        return new FanoutExchange(ORDER_COMPLETED_EXCHANGE, true, false);
    }

    @Bean
    public Queue mysqlInsertQueue() {
        return QueueBuilder.durable(MYSQL_INSERT_QUEUE)
                .withArgument("x-message-ttl", 86400000)
                .build();
    }

    @Bean
    public Queue sqlserverDistributionQueue() {
        return QueueBuilder.durable(SQLSERVER_DISTRIBUTION_QUEUE)
                .withArgument("x-message-ttl", 86400000)
                .build();
    }

    @Bean
    public Queue positionGenerateQueue() {
        return QueueBuilder.durable(POSITION_GENERATE_QUEUE)
                .withArgument("x-message-ttl", 86400000)
                .build();
    }

    @Bean
    public Binding mysqlInsertBinding(Queue mysqlInsertQueue, FanoutExchange orderCompletedExchange) {
        return BindingBuilder.bind(mysqlInsertQueue).to(orderCompletedExchange);
    }

    @Bean
    public Binding sqlserverDistributionBinding(Queue sqlserverDistributionQueue, FanoutExchange orderCompletedExchange) {
        return BindingBuilder.bind(sqlserverDistributionQueue).to(orderCompletedExchange);
    }

    @Bean
    public Binding positionGenerateBinding(Queue positionGenerateQueue, FanoutExchange orderCompletedExchange) {
        return BindingBuilder.bind(positionGenerateQueue).to(orderCompletedExchange);
    }
}
