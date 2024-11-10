package com.chat.websocket_hub.utils;

import com.chat.websocket_hub.event.Event;
import com.chat.websocket_hub.event.downstream.UserSessionStatusEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Base64;

public class EventUtils {

  private EventUtils() {}

  private static final ObjectMapper objectMapper =
      JsonMapper.builder()
          // Register JavaTimeModule to handle Java 8 date/time types
          .addModule(new JavaTimeModule())
          .build();

  public static Event buildEvent(UserSessionStatusEvent event) throws JsonProcessingException {
    String json = objectMapper.writeValueAsString(event);
    String encodedJson = encodeBase64(json);
    return Event.builder().type(event.getClass().toString()).payloadBase64(encodedJson).build();
  }

  private static String encodeBase64(String json) {
    return Base64.getEncoder().encodeToString(json.getBytes());
  }
}
