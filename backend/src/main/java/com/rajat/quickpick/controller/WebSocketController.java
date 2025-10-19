package com.rajat.quickpick.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

    @MessageMapping("/ping")
    @SendToUser("/queue/pong")
    public Map<String, Object> ping(@Payload Map<String, Object> message,
                                    SimpMessageHeaderAccessor headerAccessor) {
        String userId = (String) headerAccessor.getSessionAttributes().get("userId");
        log.info("Received ping from user: {}", userId);

        return Map.of(
                "type", "pong",
                "timestamp", System.currentTimeMillis(),
                "message", "Connection alive"
        );
    }

}