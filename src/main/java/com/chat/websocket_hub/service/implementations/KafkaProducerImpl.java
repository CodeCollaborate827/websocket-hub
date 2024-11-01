package com.chat.websocket_hub.service.implementations;

import com.chat.websocket_hub.config.ProducerBindingConfig;
import com.chat.websocket_hub.event.Event;
import com.chat.websocket_hub.event.downstream.SessionEndEvent;
import com.chat.websocket_hub.event.downstream.SessionStartEvent;
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
  public void sendSessionStartEvent(SessionStartEvent sessionStartEvent) {
    sendEvent(sessionStartEvent, ProducerBindingConfig.sessionStartDownstreamSink, "session start");
  }

  @Override
  public void sendSessionEndEvent(SessionEndEvent sessionEndEvent) {
    sendEvent(sessionEndEvent, ProducerBindingConfig.sessionEndDownstreamSink, "session end");
  }

  private <T> void sendEvent(T eventObject, Sinks.Many<Message<Event>> sink, String eventType) {
    try {
      Event event = EventUtils.buildEvent(eventObject);
      Message<Event> message = MessageBuilder.withPayload(event).build();
      Sinks.EmitResult result = sink.tryEmitNext(message);

      if (result.isFailure()) {
        log.error("Send {} event failed", eventType);
      } else {
        log.info("Send {} event success", eventType);
      }
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to process JSON for " + eventType + " event", e);
    }
  }
}
