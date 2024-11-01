package com.chat.websocket_hub.event.downstream;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionEndEvent {
  private String userId;
  private String sessionId;
  private OffsetDateTime createdAt;
}
