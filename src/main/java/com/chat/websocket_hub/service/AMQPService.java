package com.chat.websocket_hub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AMQPService {

  private final AmqpAdmin amqpAdmin;
  private final Exchange userMessageExchange;
  private final Queue userMessageQueue;

  private void addBindingExchangeRoutingForQueue(
      Queue queue, Exchange exchange, String bindingKey) {
    log.info(
        "Adding binding key: {} to queue: {} for exchange: {}",
        bindingKey,
        queue.getName(),
        exchange);
    Binding binding = BindingBuilder.bind(queue).to(exchange).with(bindingKey).noargs();

    amqpAdmin.declareBinding(binding);
  }

  private void removeBindingExchangeRoutingForQueue(
      Queue queue, Exchange exchange, String bindingKey) {
    log.info(
        "Removing binding key: {} from queue: {} for exchange: {}",
        bindingKey,
        queue.getName(),
        exchange);
    Binding binding = BindingBuilder.bind(queue).to(exchange).with(bindingKey).noargs();
    amqpAdmin.removeBinding(binding);
  }

  public void subscribeUserMessages(String userId) {
    addBindingExchangeRoutingForQueue(userMessageQueue, userMessageExchange, userId);
  }

  public void unsubscribeUserMessages(String userId) {
    removeBindingExchangeRoutingForQueue(userMessageQueue, userMessageExchange, userId);
  }
}
