package com.chat.websocket_hub.config;

import com.chat.websocket_hub.event.Event;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Slf4j
@Configuration
public class ProducerBindingConfig {
  public static final Sinks.Many<Message<Event>> sessionStartDownstreamSink =
      Sinks.many().unicast().onBackpressureBuffer();
  public static final Sinks.Many<Message<Event>> sessionEndDownstreamSink =
      Sinks.many().unicast().onBackpressureBuffer();

  @Bean("sessionStartDownstream")
  public Supplier<Flux<Message<Event>>> sessionStartDownstream() {
    return sessionStartDownstreamSink::asFlux;
  }

  @Bean("sessionEndDownstream")
  public Supplier<Flux<Message<Event>>> sessionEndDownstream() {
    return sessionEndDownstreamSink::asFlux;
  }
}
