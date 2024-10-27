package com.chat.websocket_hub.config;

import com.chat.websocket_hub.service.WebsocketSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

import java.util.HashMap;
import java.util.Map;


@Configuration
@RequiredArgsConstructor
public class WebConfig {

  private final WsMessageHandler wsMessageHandler;
  @Bean
  public HandlerMapping handlerMapping() {
    Map<String, WebSocketHandler> map = new HashMap<>();
    map.put("/ws", wsMessageHandler);
    int order = -1; // before annotated controllers

    return new SimpleUrlHandlerMapping(map, order);
  }
}
