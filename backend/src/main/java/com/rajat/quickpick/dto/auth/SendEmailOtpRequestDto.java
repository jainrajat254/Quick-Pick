package com.rajat.quickpick.dto.auth;

import com.rajat.quickpick.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SendEmailOtpRequestDto {
    @Email
    private String email;

    @NotNull
    private Role userType;
}

