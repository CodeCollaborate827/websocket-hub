package com.chat.websocket_hub.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    private static final boolean EXCHANGE_AUTO_DELETE = false; // Don't delete the exchange when the last queue is unbound from it

    //TODO: consider moving these to Config class with @ConfigurationProperties
    @Value("${rabbitmq.message.exchange.name}")
    private String topicExchangeName;
    @Value("${rabbitmq.message.exchange.durable}")
    private boolean exchangeDurable;

    @Value("${rabbitmq.message.queue.name}")
    private String queueName;
    @Value("${rabbitmq.message.queue.durable}")
    private boolean queueDurable;

    @Bean("userMessageExchange") //TODO: define bean name somewhere in constants
    public TopicExchange userMessageExchange() {
        return new TopicExchange(topicExchangeName, exchangeDurable, EXCHANGE_AUTO_DELETE);
    }

    @Bean("userMessageQueue") //TODO: define bean name somewhere in constants
    public Queue userMessageQueue() {
        return new Queue(queueName, queueDurable);
    }


}
