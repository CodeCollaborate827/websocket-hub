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
public class UserSessionStatusEvent {
  private String type; // SESSION_START, SESSION_END
  private String userId;
  private String sessionId;
  private OffsetDateTime createdAt;
}
