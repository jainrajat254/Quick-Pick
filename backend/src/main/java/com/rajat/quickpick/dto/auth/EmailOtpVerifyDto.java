package com.rajat.quickpick.dto.auth;

import com.rajat.quickpick.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmailOtpVerifyDto {
    @Email
    @NotBlank
    private String email;

    @NotNull
    private Role userType;

    @NotBlank
    private String otp;
}

