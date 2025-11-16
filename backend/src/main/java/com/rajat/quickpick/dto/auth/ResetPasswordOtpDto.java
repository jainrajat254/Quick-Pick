package com.rajat.quickpick.dto.auth;

import com.rajat.quickpick.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class ResetPasswordOtpDto {
    @Email
    @NotBlank
    private String email;

    @NotNull
    private Role userType;

    @NotBlank
    private String otp;

    @NotBlank
    @Size(min = 6)
    private String newPassword;
}
