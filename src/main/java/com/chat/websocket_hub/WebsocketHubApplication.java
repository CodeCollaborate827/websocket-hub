package com.chat.websocket_hub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class WebsocketHubApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebsocketHubApplication.class, args);
  }
}
// TODO: add log messsages DEBUG, INFO, WARN, ERROR
// TODO: implement metrics

// TODO: you need to configure the offset committing for Kafka consumer
// TODO: cleanup code
