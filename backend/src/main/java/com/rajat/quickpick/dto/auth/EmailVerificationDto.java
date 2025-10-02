package com.rajat.quickpick.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailVerificationDto {

    @NotBlank(message = "Token is required")
    private String token;

    @NotBlank(message = "Type is required")
    private String type;
}