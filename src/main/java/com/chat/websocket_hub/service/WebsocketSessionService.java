package com.chat.websocket_hub.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service
public class WebsocketSessionService {

  //  private Sinks.Many<>

  private Map<String, Sinks.Many<String>> sessionSinkMap = new ConcurrentHashMap<>();

  public Sinks.Many<String> addSession(String sessionId) {
    if (sessionId == null) return null;
    Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();
    sessionSinkMap.put(sessionId, sink);

    return sink;
  }

  public Sinks.Many<String> getSession(String sessionId) {
    if (sessionId == null) return null;
    return sessionSinkMap.get(sessionId);
  }

  public void removeSession(String sessionId) {
    if (sessionId == null) return;
    sessionSinkMap.remove(sessionId);
  }
}
