package com.rajat.quickpick.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LogoutDto {

    @NotBlank(message = "User ID is required")
    private String userId;

    private String refreshToken;
}

