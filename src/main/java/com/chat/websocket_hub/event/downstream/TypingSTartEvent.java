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
public class TypingSTartEvent {
  private String userId;
  private String conversationId;
  private OffsetDateTime createdAt;
}
