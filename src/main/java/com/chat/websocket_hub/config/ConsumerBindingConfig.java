package com.chat.websocket_hub.config;

import com.chat.websocket_hub.event.upstream.NewMessageEvent;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.messaging.Message;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ConsumerBindingConfig {

  private final WsMessageHandler wsMessageHandler;

  @Bean
  public Consumer<Flux<Message<NewMessageEvent>>> process() {
    return flux -> flux.doOnNext(msg -> process(msg)).subscribe();
  }

  private void process(Message message) {
    log.info("Received {}", message);
    NewMessageEvent payload = (NewMessageEvent) message.getPayload();
    log.info("Sending message to sessionId: {}", payload.getId());
    wsMessageHandler.sendMessageToWsSession(payload.getId(), payload.getMessage());
  }
}
