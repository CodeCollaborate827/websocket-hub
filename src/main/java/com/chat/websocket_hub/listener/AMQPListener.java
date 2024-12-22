package com.chat.websocket_hub.listener;

import com.chat.websocket_hub.config.WsMessageHandler;
import com.chat.websocket_hub.model.WebsocketMessage;
import com.chat.websocket_hub.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class AMQPListener {

    private final WsMessageHandler wsMessageHandler;

    @RabbitListener(queues = "${rabbitmq.message.queue.name}")
    public void receiveMessage(String message) {
        log.info("Received message: {}", message);
        WebsocketMessage websocketMessage = JsonUtils.fromJson(message, WebsocketMessage.class);
        String userId = websocketMessage.getUserId();
        wsMessageHandler.sendMessageToUser(userId, message);
    }


}
