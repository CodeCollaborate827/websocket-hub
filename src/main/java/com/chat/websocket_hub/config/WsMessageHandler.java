package com.chat.websocket_hub.config;

import com.chat.websocket_hub.service.WebsocketSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
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

  private final WebsocketSessionService websocketSessionService;




  @Override
  public Mono<Void> handle(WebSocketSession session) {
    String sessionId = session.getId();
    log.info("New Session connected: {}", sessionId);
    Sinks.Many<String> sink = websocketSessionService.addSession(sessionId);
    Flux<String> flux = sink.asFlux();

    DataBufferFactory bufferFactory = session.bufferFactory();
    return session.send(flux.map(
                    msg -> new WebSocketMessage(WebSocketMessage.Type.TEXT, bufferFactory.wrap(msg.getBytes()))
            ))
            .doFinally(signalType -> {
              log.info("Removing session: {}", sessionId);
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
}
