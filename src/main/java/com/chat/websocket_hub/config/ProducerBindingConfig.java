package com.chat.websocket_hub.config;

import com.chat.websocket_hub.event.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Supplier;

@Slf4j
@Configuration
public class ProducerBindingConfig {
  public static final Sinks.Many<Message<Event>> sessionDownstreamSink =
      Sinks.many().unicast().onBackpressureBuffer();

  @Bean("sessionDownstream")
  public Supplier<Flux<Message<Event>>> sessionDownstream() {
    return sessionDownstreamSink::asFlux;
  }
}
