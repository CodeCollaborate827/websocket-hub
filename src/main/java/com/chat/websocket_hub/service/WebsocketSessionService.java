package com.chat.websocket_hub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebsocketSessionService {

  //  private Sinks.Many<>
  private final AMQPService amqpService;


  // create class for session and userSessionList
  private Map<String, Sinks.Many<String>> sessionSinkMap = new ConcurrentHashMap<>();
  private Map<String, List<String>> userSessionSinkMap = new ConcurrentHashMap<>();
  public Sinks.Many<String> addSessionForUser(String userId, String sessionId) {

    userSessionSinkMap.putIfAbsent(userId, new ArrayList<>()); // make sure user has a session list
    List<String> sessionListOfUser = userSessionSinkMap.get(userId);

    if (sessionId == null) {
        log.warn("Cannot add session with null sessionId for user {}", userId);
        return null;
    }
    Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();
    sessionSinkMap.put(sessionId, sink); // add session to session map
    sessionListOfUser.add(sessionId); // add session to user session list
    // add session to user session

    return sink;
  }

  public Sinks.Many<String> getSessionBySessionId(String sessionId) {
    if (sessionId == null) return null;
    return sessionSinkMap.get(sessionId);
  }

  public List<String> getSessionsByUserId(String userId) {
      return userSessionSinkMap.getOrDefault(userId, new ArrayList<>());
  }

  public void removeSessionForUser(String userId, String sessionId) {
    List<String> sessionListOfUser = userSessionSinkMap.get(userId);
    if (sessionListOfUser == null) {
      log.warn("User {} does not have any session", userId);
      return;
    }

    if (sessionId == null) {
      log.warn("Cannot remove session with null sessionId");
      return;
    }
    sessionSinkMap.remove(sessionId); // remove session from session map
    sessionListOfUser.removeIf(session -> session.equals(sessionId)); // remove session from user session list
  }
}
