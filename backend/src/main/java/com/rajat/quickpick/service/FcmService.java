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
        log.info("fcm save device token start");
        log.info("fcm save userid {}", userId);
        log.info("fcm save usertype {}", userType);
        log.info("fcm save deviceid {}", deviceId);
        log.info("fcm save devicename {}", deviceName);
        log.info("fcm save token present {}", fcmToken != null);
        log.info("fcm save token length {}", fcmToken != null ? fcmToken.length() : 0);
        log.info("fcm save token first 20 chars {}", fcmToken != null ? fcmToken.substring(0, Math.min(20, fcmToken.length())) : "NULL");

        if (fcmToken == null || fcmToken.trim().isEmpty()) {
            log.error("fcm save critical fcm token is null or empty cannot save");
            throw new IllegalArgumentException("FCM token cannot be null or empty");
        }

        if (userId == null || userId.trim().isEmpty()) {
            log.error("fcm save critical userid is null or empty cannot save");
            throw new IllegalArgumentException("UserId cannot be null or empty");
        }

        deviceTokenRepository.deleteAllByUserId(userId);

        DeviceToken deviceToken = new DeviceToken();
        deviceToken.setUserId(userId);
        deviceToken.setUserType(userType);
        deviceToken.setFcmToken(fcmToken);
        deviceToken.setDeviceId(deviceId);
        deviceToken.setDeviceName(deviceName);
        deviceToken.setActive(true);
        deviceToken.setCreatedAt(LocalDateTime.now());
        deviceToken.setUpdatedAt(LocalDateTime.now());

        DeviceToken saved = deviceTokenRepository.save(deviceToken);
        log.info("fcm save new token created successfully id {}", saved.getId());
        log.info("fcm save single device enforcement complete only this device will receive notifications");

        List<DeviceToken> activeTokens = deviceTokenRepository.findByUserIdAndActive(userId, true);
        log.info("fcm save verification active tokens count {} should be 1", activeTokens.size());

        log.info("fcm save device token complete");
    }

    public void sendNotification(String userId, String title, String body, Map<String, String> data) {
        log.info("fcm send notification start");
        log.info("fcm send target userid {}", userId);
        log.info("fcm send title {}", title);
        log.info("fcm send body {}", body);
        log.info("fcm send data payload {}", data);

        log.info("fcm send querying database for active tokens");
        List<DeviceToken> tokens = deviceTokenRepository.findByUserIdAndActive(userId, true);

        log.info("fcm send query returned {} active device tokens for user {}", tokens.size(), userId);

        if (tokens.isEmpty()) {
            log.warn("fcm send no active fcm tokens found for user {}", userId);
            log.warn("fcm send checking if user has any tokens including inactive");

            List<DeviceToken> allUserTokens = deviceTokenRepository.findByUserId(userId);
            log.warn("fcm send total tokens for user active and inactive count {}", allUserTokens.size());

            if (allUserTokens.isEmpty()) {
                log.error("fcm send critical user has never registered any fcm tokens");
                log.error("fcm send user {} needs to call post api fcm token to register their device", userId);
            } else {
                log.warn("fcm send user has {} total tokens but all are marked as inactive", allUserTokens.size());
                for (int i = 0; i < allUserTokens.size(); i++) {
                    DeviceToken token = allUserTokens.get(i);
                    log.warn("fcm send token number {} deviceid {} active {} updatedat {}",
                            i + 1, token.getDeviceId(), token.isActive(), token.getUpdatedAt());
                }
            }

            log.warn("fcm send cannot send notification no valid tokens available");
            return;
        }

        log.info("fcm send found {} valid tokens preparing to send notifications", tokens.size());
        for (int i = 0; i < tokens.size(); i++) {
            DeviceToken token = tokens.get(i);
            log.info("fcm send token number {} deviceid {} devicename {} tokenlength {}",
                    i + 1, token.getDeviceId(), token.getDeviceName(), token.getFcmToken().length());
        }

        int successCount = 0;
        int failureCount = 0;

        for (DeviceToken deviceToken : tokens) {
            try {
                log.info("fcm send sending to device {} name {}", deviceToken.getDeviceId(), deviceToken.getDeviceName());
                log.info("fcm send token first 20 chars {}", deviceToken.getFcmToken().substring(0, Math.min(20, deviceToken.getFcmToken().length())));

                log.info("fcm send building firebase message");
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

                log.info("fcm send message constructed successfully");
                log.info("fcm send calling firebase messaging send");
                String response = FirebaseMessaging.getInstance().send(message);
                successCount++;
                log.info("fcm send success notification sent to device {}", deviceToken.getDeviceId());
                log.info("fcm send firebase response {}", response);

            } catch (FirebaseMessagingException e) {
                failureCount++;
                log.error("fcm send failed to send notification to device {}", deviceToken.getDeviceId());
                log.error("fcm send firebase messaging exception occurred");
                log.error("fcm send error code {}", e.getErrorCode());
                log.error("fcm send error message {}", e.getMessage());
                log.error("fcm send messaging error code enum {}", e.getMessagingErrorCode());
                log.error("fcm send full stack trace", e);

                String errorCode = e.getErrorCode() != null ? e.getErrorCode().name() : "UNKNOWN";
                log.error("fcm send processing error code {}", errorCode);

                if ("INVALID_ARGUMENT".equals(errorCode) || "UNREGISTERED".equals(errorCode)) {
                    log.warn("fcm send token is invalid or unregistered marking as inactive in db");
                    deviceToken.setActive(false);
                    deviceToken.setUpdatedAt(LocalDateTime.now());
                    deviceTokenRepository.save(deviceToken);
                    log.info("fcm send token marked as inactive id {}", deviceToken.getId());
                }
            } catch (Exception e) {
                failureCount++;
                log.error("fcm send unexpected error sending notification to device {}", deviceToken.getDeviceId());
                log.error("fcm send exception type {}", e.getClass().getName());
                log.error("fcm send error message {}", e.getMessage());
                log.error("fcm send full stack trace", e);
            }
        }

        log.info("fcm send notification complete");
        log.info("fcm send successful sends {}", successCount);
        log.info("fcm send failed sends {}", failureCount);
        log.info("fcm send total attempts {}", tokens.size());
    }

    public void removeDeviceToken(String userId, String deviceId) {
        log.info("fcm remove device token");
        log.info("fcm remove userid {}", userId);
        log.info("fcm remove deviceid {}", deviceId);

        deviceTokenRepository.findByUserIdAndDeviceId(userId, deviceId).ifPresent(token -> {
            log.info("fcm remove found token to remove id {}", token.getId());
            token.setActive(false);
            token.setUpdatedAt(LocalDateTime.now());
            deviceTokenRepository.save(token);
            log.info("fcm remove token marked as inactive for user {} on device {}", userId, deviceId);
        });

        log.info("fcm remove device token complete");
    }

    public void removeAllUserTokens(String userId) {
        log.info("fcm remove all user tokens");
        log.info("fcm remove all userid {}", userId);

        List<DeviceToken> tokens = deviceTokenRepository.findByUserId(userId);
        log.info("fcm remove all found {} tokens to delete", tokens.size());

        deviceTokenRepository.deleteByUserId(userId);
        log.info("fcm remove all fcm tokens removed for user {}", userId);
        log.info("fcm remove all user tokens complete");
    }
}
