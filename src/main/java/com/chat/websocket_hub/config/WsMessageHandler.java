package com.chat.websocket_hub.config;

import com.chat.websocket_hub.constants.ApplicationConstants;
import com.chat.websocket_hub.event.downstream.Session;
import com.chat.websocket_hub.service.KafkaProducer;
import com.chat.websocket_hub.service.WebsocketSessionService;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.CloseStatus;
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
  private final String CLIENT_IP = "clientIp";

  @NotNull
  @Override
  public Mono<Void> handle(WebSocketSession session) {
    String sessionId = session.getId();

    String userId = extractUserIdFromHeader(session);
    if (userId == null) {
      log.error("Unauthorized access attempt for session: {}", sessionId);
      return session.close(CloseStatus.POLICY_VIOLATION);
    }

    String clientIp = extractClientIdFromHeader(session);

    // Extract userId and handle unauthorized access
    return Mono.just(userId)
        .switchIfEmpty(
            Mono.defer(
                () -> {
                  log.error("Unauthorized access attempt for session: {}", sessionId);
                  return session.close(CloseStatus.POLICY_VIOLATION).then(Mono.empty());
                }))
        .flatMap(
            u -> {
              log.info("New Session started: {}, userId: {}", sessionId, userId);
              Sinks.Many<String> sink = websocketSessionService.addSession(sessionId);

              Session sessionStartEvent =
                  Session.builder()
                      .sessionId(sessionId)
                      .userId(userId)
                      .status(ApplicationConstants.SESSION_START)
                      .clientIp(clientIp)
                      .timestamp(Instant.now().getEpochSecond())
                      .build();
              kafkaProducer.sendSessionEvent(sessionStartEvent);

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
                        Session sessionEndEvent =
                            Session.builder()
                                .sessionId(sessionId)
                                .userId(userId)
                                .clientIp(clientIp)
                                .status(ApplicationConstants.SESSION_END)
                                .timestamp(Instant.now().getEpochSecond())
                                .build();
                        kafkaProducer.sendSessionEvent(sessionEndEvent);

                        websocketSessionService.removeSession(sessionId);
                      });
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
    if (userIdHeader == null || userIdHeader.isEmpty()) {
      return null;
    }
    return userIdHeader.getFirst();
  }

  private String extractClientIdFromHeader(WebSocketSession session) {
    List<String> clientIdHeader = session.getHandshakeInfo().getHeaders().get(CLIENT_IP);
    if (clientIdHeader == null || clientIdHeader.isEmpty()) {
      return null;
    }
    return clientIdHeader.getFirst();
  }
}
