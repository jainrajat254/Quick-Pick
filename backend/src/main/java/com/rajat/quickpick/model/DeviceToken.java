package com.rajat.quickpick.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "device_tokens")
public class DeviceToken {

    @Id
    private String id;

    @Indexed
    private String userId;

    private String userType; // USER or VENDOR

    @Indexed
    private String fcmToken;

    private String deviceId;

    private String deviceName;

    private boolean active = true;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}