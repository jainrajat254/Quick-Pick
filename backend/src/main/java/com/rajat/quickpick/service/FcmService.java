package com.rajat.quickpick.service;

import com.google.firebase.messaging.*;
import com.rajat.quickpick.model.DeviceToken;
import com.rajat.quickpick.repository.DeviceTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmService {

    private final DeviceTokenRepository deviceTokenRepository;

    public void saveDeviceToken(String userId, String userType, String fcmToken, String deviceId, String deviceName) {
        DeviceToken existingToken = deviceTokenRepository.findByUserIdAndDeviceId(userId, deviceId)
                .orElse(null);

        if (existingToken != null) {
            existingToken.setFcmToken(fcmToken);
            existingToken.setDeviceName(deviceName);
            existingToken.setActive(true);
            existingToken.setUpdatedAt(LocalDateTime.now());
            deviceTokenRepository.save(existingToken);
        } else {
            DeviceToken deviceToken = new DeviceToken();
            deviceToken.setUserId(userId);
            deviceToken.setUserType(userType);
            deviceToken.setFcmToken(fcmToken);
            deviceToken.setDeviceId(deviceId);
            deviceToken.setDeviceName(deviceName);
            deviceToken.setActive(true);
            deviceToken.setCreatedAt(LocalDateTime.now());
            deviceToken.setUpdatedAt(LocalDateTime.now());
            deviceTokenRepository.save(deviceToken);
        }

        log.info("FCM token saved for user: {} on device: {}", userId, deviceId);
    }

    public void sendNotification(String userId, String title, String body, Map<String, String> data) {
        List<DeviceToken> tokens = deviceTokenRepository.findByUserIdAndActive(userId, true);

        if (tokens.isEmpty()) {
            log.warn("No active FCM tokens found for user: {}", userId);
            return;
        }
        for (DeviceToken deviceToken : tokens) {
            try {
                Message message = Message.builder()
                        .setToken(deviceToken.getFcmToken())
                        .setNotification(Notification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .build())
                        .putAllData(data != null ? data : new HashMap<>())
                        .setAndroidConfig(AndroidConfig.builder()
                                .setPriority(AndroidConfig.Priority.HIGH)
                                .setNotification(AndroidNotification.builder()
                                        .setSound("default")
                                        .setChannelId("orders")
                                        .build())
                                .build())
                        .build();
                String response = FirebaseMessaging.getInstance().send(message);
                log.info("Successfully sent FCM notification to user: {} - Response: {}", userId, response);

            } catch (FirebaseMessagingException e) {
                log.error("Failed to send FCM notification to user: {} - Error: {}", userId, e.getMessage());

                if (e.getErrorCode().equals("INVALID_ARGUMENT") || e.getErrorCode().equals("UNREGISTERED")) {
                    deviceToken.setActive(false);
                    deviceToken.setUpdatedAt(LocalDateTime.now());
                    deviceTokenRepository.save(deviceToken);
                }
            }
        }
    }

    public void removeDeviceToken(String userId, String deviceId) {
        deviceTokenRepository.findByUserIdAndDeviceId(userId, deviceId).ifPresent(token -> {
            token.setActive(false);
            token.setUpdatedAt(LocalDateTime.now());
            deviceTokenRepository.save(token);
            log.info("FCM token removed for user: {} on device: {}", userId, deviceId);
        });
    }

    public void removeAllUserTokens(String userId) {
        deviceTokenRepository.deleteByUserId(userId);
        log.info("All FCM tokens removed for user: {}", userId);
    }
}