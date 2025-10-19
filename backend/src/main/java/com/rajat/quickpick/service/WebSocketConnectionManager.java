package com.rajat.quickpick.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class WebSocketConnectionManager {

    private final ConcurrentHashMap<String, String> userSessions = new ConcurrentHashMap<>();

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        String userId = (String) headerAccessor.getSessionAttributes().get("userId");

        if (userId != null) {
            userSessions.put(sessionId, userId);
            log.info("User {} connected with session {}", userId, sessionId);
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        String userId = userSessions.remove(sessionId);

        if (userId != null) {
            log.info("User {} disconnected from session {}", userId, sessionId);
        }
    }

    public boolean isUserConnected(String userId) {
        return userSessions.containsValue(userId);
    }

    public int getConnectedUserCount() {
        return userSessions.size();
    }
}