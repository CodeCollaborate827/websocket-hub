package com.chat.websocket_hub.model;

import lombok.Data;

@Data
public class KafkaMessage {
  private String id;
  private String message;
}
