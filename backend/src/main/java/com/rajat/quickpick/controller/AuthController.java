package com.rajat.quickpick.controller;


import com.rajat.quickpick.dto.auth.*;
import com.rajat.quickpick.dto.user.LoginUserDto;
import com.rajat.quickpick.dto.user.RegisterUserDto;
import com.rajat.quickpick.dto.vendor.LoginVendorDto;
import com.rajat.quickpick.dto.vendor.RegisterVendor;
import com.rajat.quickpick.enums.Role;
import com.rajat.quickpick.security.JwtUtil;
import com.rajat.quickpick.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register/user")
    public ResponseEntity<AuthResponseDto> registerUser(@Valid @RequestBody RegisterUserDto registrationDto) {
        AuthResponseDto response = authService.registerUser(registrationDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register/vendor")
    public ResponseEntity<AuthResponseDto> registerVendor(@Valid @RequestBody RegisterVendor registrationDto) {
        AuthResponseDto response = authService.registerVendor(registrationDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/user")
    public ResponseEntity<AuthResponseDto> loginUser(@Valid @RequestBody LoginUserDto loginDto) {
        AuthResponseDto response = authService.login(loginDto.getEmail(), loginDto.getPassword(), "USER");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/vendor")
    public ResponseEntity<AuthResponseDto> loginVendor(@Valid @RequestBody LoginVendorDto loginDto) {
        AuthResponseDto response = authService.login(loginDto.getEmail(), loginDto.getPassword(), "VENDOR");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Map<String, String>> verifyEmail(@RequestParam String token, @RequestParam String type) {
        authService.verifyEmail(token, type);
        return ResponseEntity.ok(Map.of("message", "Email verified successfully"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@Valid @RequestBody ForgotPasswordDto forgotPasswordDto) {
        authService.forgotPassword(forgotPasswordDto);
        return ResponseEntity.ok(Map.of("message", "Password reset email sent"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto) {
        authService.resetPassword(resetPasswordDto);
        return ResponseEntity.ok(Map.of("message", "Password reset successful"));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto,
                                                              Authentication authentication) {
        String email = authentication.getName();
        authService.changePassword(email, changePasswordDto);
        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<Map<String, String>> resendVerificationEmail(@RequestParam String email,
                                                                       @RequestParam Role userType) {
        authService.resendVerificationEmail(email, userType);
        return ResponseEntity.ok(Map.of("message", "Verification email sent"));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDto> refreshToken(@Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        AuthResponseDto response = authService.refreshToken(refreshTokenDto.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        authService.logout(refreshTokenDto.getRefreshToken());
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
}