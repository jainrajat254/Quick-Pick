package com.rajat.quickpick.model;

import com.rajat.quickpick.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "pending_users")
public class PendingUser {

    @Id
    private String id;

    private String fullName;
    private String phone;
    private String email;
    private String password;
    private String collegeName;

    private Role role = Role.STUDENT;

    private String otp;
    private LocalDateTime otpCreatedAt;
    private LocalDateTime otpExpiresAt;
    private Integer otpAttempts;

    private Integer otpSendCount;
    private LocalDateTime otpLastSentAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
