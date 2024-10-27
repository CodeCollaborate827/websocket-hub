package com.chat.websocket_hub.config;

import com.chat.websocket_hub.model.KafkaMessage;
import com.chat.websocket_hub.service.WebsocketSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.Message;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ConsumerBindingConfig {

  private final WsMessageHandler wsMessageHandler;


  @Bean
  public Consumer<Flux<Message<KafkaMessage>>> process() {
    return flux -> flux.doOnNext(msg -> process(msg))
            .subscribe();
  }


  private void process(Message message) {
    log.info("Received {}", message);
    KafkaMessage payload = (KafkaMessage) message.getPayload();
    log.info("Sending message to sessionId: {}", payload.getId());
    wsMessageHandler.sendMessageToWsSession(payload.getId(), payload.getMessage());
  }
}
