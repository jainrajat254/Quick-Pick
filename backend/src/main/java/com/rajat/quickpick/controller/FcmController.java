package com.rajat.quickpick.controller;

import com.rajat.quickpick.security.JwtUtil;
import com.rajat.quickpick.service.FcmService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/fcm")
@RequiredArgsConstructor
@Slf4j
public class FcmController {

    private final FcmService fcmService;
    private final JwtUtil jwtUtil;

    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> saveToken(
            @RequestBody Map<String, String> request,
            HttpServletRequest httpRequest) {

        log.info("fcm controller save token request");
        log.info("fcm ctrl request body {}", request);

        String userId = extractUserIdFromToken(httpRequest);
        String userType = extractUserTypeFromToken(httpRequest);
        String fcmToken = request.get("fcmToken");
        String deviceId = request.get("deviceId");
        String deviceName = request.get("deviceName");

        log.info("fcm ctrl extracted userid {}", userId);
        log.info("fcm ctrl extracted usertype {}", userType);
        log.info("fcm ctrl fcm token present {}", fcmToken != null);
        log.info("fcm ctrl deviceid {}", deviceId);
        log.info("fcm ctrl devicename {}", deviceName);

        fcmService.saveDeviceToken(userId, userType, fcmToken, deviceId, deviceName);

        log.info("fcm ctrl token save completed successfully");
        return ResponseEntity.ok(Map.of("message", "FCM token saved successfully"));
    }

    @DeleteMapping("/token")
    public ResponseEntity<Map<String, String>> removeToken(
            @RequestBody Map<String, String> request,
            HttpServletRequest httpRequest) {

        log.info("fcm controller remove token request");
        String userId = extractUserIdFromToken(httpRequest);
        String deviceId = request.get("deviceId");

        log.info("fcm ctrl userid {}", userId);
        log.info("fcm ctrl deviceid {}", deviceId);

        fcmService.removeDeviceToken(userId, deviceId);

        log.info("fcm ctrl token removal completed");
        return ResponseEntity.ok(Map.of("message", "FCM token removed successfully"));
    }

    private String extractUserIdFromToken(HttpServletRequest request) {
        log.debug("fcm ctrl extracting userid from token");
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String userId = jwtUtil.extractUserId(token);
            log.debug("fcm ctrl extracted userid {}", userId);
            return userId;
        }
        log.error("fcm ctrl invalid or missing authorization header");
        throw new RuntimeException("Invalid token");
    }

    private String extractUserTypeFromToken(HttpServletRequest request) {
        log.debug("fcm ctrl extracting usertype from token");
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String userType = jwtUtil.extractRole(token);
            log.debug("fcm ctrl extracted usertype {}", userType);
            return userType;
        }
        log.error("fcm ctrl invalid or missing authorization header");
        throw new RuntimeException("Invalid token");
    }
}