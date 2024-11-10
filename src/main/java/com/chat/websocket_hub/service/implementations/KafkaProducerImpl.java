package com.chat.websocket_hub.service.implementations;

import com.chat.websocket_hub.config.ProducerBindingConfig;
import com.chat.websocket_hub.event.Event;
import com.chat.websocket_hub.event.downstream.UserSessionStatusEvent;
import com.chat.websocket_hub.service.KafkaProducer;
import com.chat.websocket_hub.utils.EventUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerImpl implements KafkaProducer {

  @Override
  public void sendSessionEvent(UserSessionStatusEvent sessionEvent) {
    try {
      Event event = EventUtils.buildEvent(sessionEvent);
      Message<Event> message = MessageBuilder.withPayload(event).build();
      Sinks.EmitResult result = ProducerBindingConfig.sessionDownstreamSink.tryEmitNext(message);

      if (result.isFailure()) {
        log.error("Send {} event failed", sessionEvent.getType());
      } else {
        log.info("Send {} event success", sessionEvent.getType());
      }
    } catch (JsonProcessingException e) {
      throw new RuntimeException(
          "Failed to process JSON for " + sessionEvent.getType() + " event", e);
    }
  }
}
