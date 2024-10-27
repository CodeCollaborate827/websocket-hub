package com.chat.websocket_hub.event.downstream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionEndEvent {
  private String userId;
  private String sessionId;
  private OffsetDateTime createdAt;
}
