package com.chat.websocket_hub.event.upstream;

import lombok.Data;

@Data
public class NewMessageEvent {
  private String id;
  private String message;
}
