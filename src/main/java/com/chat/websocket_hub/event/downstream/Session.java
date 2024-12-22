package com.chat.websocket_hub.event.downstream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Session {
  private String status; // SESSION_START, SESSION_END
  private String userId;
  private String sessionId;
  private String clientIp;
  private Long timestamp;
}
