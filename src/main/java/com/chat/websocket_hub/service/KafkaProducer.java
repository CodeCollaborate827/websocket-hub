package com.chat.websocket_hub.service;

import com.chat.websocket_hub.event.downstream.SessionEndEvent;
import com.chat.websocket_hub.event.downstream.SessionStartEvent;

public interface KafkaProducer {
  void sendSessionStartEvent(SessionStartEvent data);

  void sendSessionEndEvent(SessionEndEvent data);
}
