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
@Document(collection = "email_verification_tokens")
public class EmailVerificationToken {

    @Id
    private String id;

    private String token;
    private String email;
    private Role userType;
    private LocalDateTime expiryDate;
    private boolean used;

    private LocalDateTime createdAt;

    private Integer attempts;
    private LocalDateTime lastSentAt;
}