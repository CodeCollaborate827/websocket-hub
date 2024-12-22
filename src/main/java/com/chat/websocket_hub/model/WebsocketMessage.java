package com.chat.websocket_hub.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// this message is used to send messages to the client via websocket
public class WebsocketMessage {

  public enum EventType {
    MESSAGE_EVENT,
    NOTIFICATION_EVENT,
    CONVERSATION_EVENT,
  }

  private String userId;
  private String wsMessageId;
  private EventType eventType;
  private Object payload;
  private Long timestamp;
}
