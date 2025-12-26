package com.rajat.quickpick.model;

import com.rajat.quickpick.enums.Role;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")

public class User {

    @Id
    private String id;

    private String fullName;
    private String phone;
    private String profileImageUrl;

    private boolean isPhoneVerified;
    private boolean isEmailVerified;

    private String email;
    private String password;

    private String collegeName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Role role = Role.STUDENT;

    private boolean suspended = false;
    private String suspensionReason;
    private LocalDateTime suspendedAt;


}