package com.chat.websocket_hub.config;

import com.chat.websocket_hub.event.downstream.SessionEndEvent;
import com.chat.websocket_hub.event.downstream.SessionStartEvent;
import com.chat.websocket_hub.service.KafkaProducer;
import com.chat.websocket_hub.service.WebsocketSessionService;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Component
@Slf4j
@RequiredArgsConstructor
public class WsMessageHandler implements WebSocketHandler {
  private final KafkaProducer kafkaProducer;
  private final WebsocketSessionService websocketSessionService;
  private final String USER_ID = "userId";

  @Override
  public Mono<Void> handle(WebSocketSession session) {
    String sessionId = session.getId();
    String userId = extractUserIdFromHeader(session);
    // TODO: handle exception when user id is null, return 401 Unauthorized
    log.info("New Session started: {}, userId: {}", sessionId, userId);
    Sinks.Many<String> sink = websocketSessionService.addSession(sessionId);

    SessionStartEvent sessionStartEvent =
        SessionStartEvent.builder()
            .sessionId(sessionId)
            .userId(userId)
            .createdAt(OffsetDateTime.now())
            .build();
    kafkaProducer.sendSessionStartEvent(sessionStartEvent);

    Flux<String> flux = sink.asFlux();

    DataBufferFactory bufferFactory = session.bufferFactory();
    return session
        .send(
            flux.map(
                msg ->
                    new WebSocketMessage(
                        WebSocketMessage.Type.TEXT, bufferFactory.wrap(msg.getBytes()))))
        .doFinally(
            signalType -> {
              log.info("Session ended: {}", sessionId);
              SessionEndEvent sessionEndEvent =
                  SessionEndEvent.builder()
                      .sessionId(sessionId)
                      .userId(userId)
                      .createdAt(OffsetDateTime.now())
                      .build();
              kafkaProducer.sendSessionEndEvent(sessionEndEvent);

              websocketSessionService.removeSession(sessionId);
            });
  }

  public void sendMessageToWsSession(String sessionId, String message) {
    Sinks.Many<String> session = websocketSessionService.getSession(sessionId);
    if (session == null) {
      log.error("Websocket session not found for id: {}", sessionId);
      return;
    }

    session.tryEmitNext(message);
  }

  private String extractUserIdFromHeader(WebSocketSession session) {
    List<String> userIdHeader = session.getHandshakeInfo().getHeaders().get(USER_ID);
    if (userIdHeader != null || userIdHeader.isEmpty()) {
      return null;
    }
    return userIdHeader.get(0);
  }
}
