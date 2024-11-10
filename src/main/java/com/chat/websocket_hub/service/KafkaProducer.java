package com.chat.websocket_hub.service;

import com.chat.websocket_hub.event.downstream.UserSessionStatusEvent;

public interface KafkaProducer {
  void sendSessionEvent(UserSessionStatusEvent data);
}
