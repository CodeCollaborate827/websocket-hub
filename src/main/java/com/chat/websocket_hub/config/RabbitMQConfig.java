package com.chat.websocket_hub;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    private static final boolean EXCHANGE_AUTO_DELETE = false; // Don't delete the exchange when the last queue is unbound from it



}
