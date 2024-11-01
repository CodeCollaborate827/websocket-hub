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
public class TypingSTartEvent {
  private String userId;
  private String conversationId;
  private OffsetDateTime createdAt;
}
