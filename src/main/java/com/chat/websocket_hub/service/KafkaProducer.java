package com.chat.websocket_hub.service;

import com.chat.websocket_hub.event.downstream.Session;

public interface KafkaProducer {
  void sendSessionEvent(Session data);
}
