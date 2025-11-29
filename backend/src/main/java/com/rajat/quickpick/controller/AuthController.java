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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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


    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@Valid @RequestBody ForgotPasswordDto forgotPasswordDto) {
        authService.forgotPassword(forgotPasswordDto);
        return ResponseEntity.ok(Map.of("message", "If the account exists, a password reset code has been sent"));
    }

    //while working with postman... this endpoint can be used to reset password
    @GetMapping(value = "/reset-password", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> showResetPasswordForm(@RequestParam String token, @RequestParam String type) {
        try {
            authService.validateResetToken(token, type);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    "<!DOCTYPE html><html><head><title>Invalid Token</title><style>body{font-family:Arial;padding:50px;text-align:center;}</style></head><body>" +
                    "<h2>Invalid or Expired Token</h2><p>" + e.getMessage() + "</p>" +
                    "<p>Please request a new password reset link.</p></body></html>"
            );
        }

        String html = "<!DOCTYPE html>" +
                "<html><head><title>Reset Password</title>" +
                "<style>" +
                "body{font-family:Arial,sans-serif;background:#f4f4f4;padding:20px;}" +
                ".container{max-width:400px;margin:50px auto;background:white;padding:30px;border-radius:8px;box-shadow:0 2px 10px rgba(0,0,0,0.1);}" +
                "h2{color:#333;text-align:center;}" +
                "label{display:block;margin:15px 0 5px;color:#555;font-weight:bold;}" +
                "input{width:100%;padding:10px;border:1px solid #ddd;border-radius:4px;box-sizing:border-box;font-size:14px;}" +
                "button{width:100%;padding:12px;background:#007bff;color:white;border:none;border-radius:4px;cursor:pointer;font-size:16px;margin-top:20px;}" +
                "button:hover{background:#0056b3;}" +
                ".error{color:red;font-size:12px;margin-top:5px;display:none;}" +
                ".success{color:green;text-align:center;margin-top:20px;display:none;}" +
                "</style></head><body>" +
                "<div class='container'>" +
                "<h2>Reset Your Password</h2>" +
                "<form id='resetForm'>" +
                "<label>New Password:</label>" +
                "<input type='password' id='newPassword' required minlength='6' placeholder='Enter new password'>" +
                "<div class='error' id='passError'>Password must be at least 6 characters</div>" +
                "<label>Confirm Password:</label>" +
                "<input type='password' id='confirmPassword' required placeholder='Confirm new password'>" +
                "<div class='error' id='confirmError'>Passwords do not match</div>" +
                "<button type='submit'>Reset Password</button>" +
                "<div class='success' id='successMsg'>Password reset successful! You can now login.</div>" +
                "</form></div>" +
                "<script>" +
                "document.getElementById('resetForm').addEventListener('submit', async function(e) {" +
                "e.preventDefault();" +
                "var newPass = document.getElementById('newPassword').value;" +
                "var confirmPass = document.getElementById('confirmPassword').value;" +
                "document.getElementById('passError').style.display = 'none';" +
                "document.getElementById('confirmError').style.display = 'none';" +
                "if(newPass.length < 6) {document.getElementById('passError').style.display = 'block'; return;}" +
                "if(newPass !== confirmPass) {document.getElementById('confirmError').style.display = 'block'; return;}" +
                "try {" +
                "const response = await fetch('/api/auth/reset-password', {" +
                "method: 'POST'," +
                "headers: {'Content-Type': 'application/json'}," +
                "body: JSON.stringify({token: '" + token + "', type: '" + type + "', newPassword: newPass})" +
                "});" +
                "if(response.ok) {" +
                "document.getElementById('successMsg').style.display = 'block';" +
                "document.getElementById('resetForm').reset();" +
                "setTimeout(() => window.close(), 3000);" +
                "} else {" +
                "const data = await response.json();" +
                "alert('Error: ' + (data.message || 'Failed to reset password'));" +
                "}" +
                "} catch(error) {alert('Network error. Please try again.');}" +
                "});" +
                "</script></body></html>";

        return ResponseEntity.ok(html);
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


    @PostMapping("/verify-email-otp")
    public ResponseEntity<Map<String, String>> verifyEmailOtp(@Valid @RequestBody EmailOtpVerifyDto verifyDto) {
        authService.verifyEmailOtp(verifyDto);
        return ResponseEntity.ok(Map.of("message", "Email verified successfully"));
    }

    @PostMapping("/send-password-otp")
    public ResponseEntity<Map<String,String>> sendPasswordOtp(@Valid @RequestBody PasswordOtpRequestDto dto) {
        authService.sendPasswordResetOtp(dto);
        return ResponseEntity.ok(Map.of("message","If the account exists, a password reset code has been sent"));
    }

    @PostMapping("/reset-password-otp")
    public ResponseEntity<Map<String,String>> resetPasswordWithOtp(@Valid @RequestBody ResetPasswordOtpDto dto) {
        authService.resetPasswordWithOtp(dto);
        return ResponseEntity.ok(Map.of("message","Password reset successful"));
    }
}