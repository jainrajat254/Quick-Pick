package com.rajat.quickpick.controller;

import com.rajat.quickpick.security.JwtUtil;
import com.rajat.quickpick.service.FcmService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/fcm")
@RequiredArgsConstructor
public class FcmController {

    private final FcmService fcmService;
    private final JwtUtil jwtUtil;

    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> saveToken(
            @RequestBody Map<String, String> request,
            HttpServletRequest httpRequest) {

        String userId = extractUserIdFromToken(httpRequest);
        String userType = extractUserTypeFromToken(httpRequest);
        String fcmToken = request.get("fcmToken");
        String deviceId = request.get("deviceId");
        String deviceName = request.get("deviceName");

        fcmService.saveDeviceToken(userId, userType, fcmToken, deviceId, deviceName);

        return ResponseEntity.ok(Map.of("message", "FCM token saved successfully"));
    }

    @DeleteMapping("/token")
    public ResponseEntity<Map<String, String>> removeToken(
            @RequestBody Map<String, String> request,
            HttpServletRequest httpRequest) {

        String userId = extractUserIdFromToken(httpRequest);
        String deviceId = request.get("deviceId");

        fcmService.removeDeviceToken(userId, deviceId);

        return ResponseEntity.ok(Map.of("message", "FCM token removed successfully"));
    }

    private String extractUserIdFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtUtil.extractUserId(token);
        }
        throw new RuntimeException("Invalid token");
    }

    private String extractUserTypeFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtUtil.extractRole(token);
        }
        throw new RuntimeException("Invalid token");
    }
}