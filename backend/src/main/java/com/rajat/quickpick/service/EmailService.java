package com.rajat.quickpick.service;

import com.rajat.quickpick.enums.Role;
import com.rajat.quickpick.utils.Secrets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    @Autowired
    private JavaMailSender mailSender;
    @Value("${app.mail.from}")
    private String fromEmail;
    @Value("${app.baseUrl}")
    private String baseUrl;

    public void sendVerificationEmail(String toEmail, String token, Role userType) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("QuickPick - Email Verification");

            String verificationUrl = baseUrl + "/api/auth/verify-email?token=" + token + "&type=" + userType;

            long expirationHours = Secrets.EMAIL_VERIFICATION_TOKEN_EXPIRATION / (1000 * 60 * 60);


            String body = "Welcome to QuickPick!\n\n" +
                    "Please click the link below to verify your email address:\n" +
                    verificationUrl + "\n\n" +
                    "This link will expire in " + expirationHours + "hours+.\n\n" +
                    "If you didn't create an account, please ignore this email.";

            message.setText(body);
            mailSender.send(message);
            log.info("Verification email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Error sending verification email to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send verification email");
        }
    }

    public void sendPasswordResetEmail(String toEmail, String token, Role userType) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("QuickPick - Password Reset");

            String resetUrl = baseUrl + "/api/auth/reset-password?token=" + token + "&type=" + userType;

            String expirationHours = String.valueOf(Secrets.PASSWORD_RESET_TOKEN_EXPIRATION / (1000 * 60 * 60));
            String body = "You requested a password reset for your QuickPick account.\n\n" +
                    "Please click the link below to reset your password:\n" +
                    resetUrl + "\n\n" +
                    "This link will expire in " + expirationHours + " hour.\n\n" +
                    "If you didn't request this reset, please ignore this email.";

            message.setText(body);
            mailSender.send(message);
            log.info("Password reset email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Error sending password reset email to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send password reset email");
        }
    }

    public void sendEmailVerificationOtp(String toEmail, String otp, Role userType, long expiryMinutes) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("QuickPick - Your Verification Code");

            String body = "Your verification code is: " + otp + "\n\n" +
                    "This code will expire in " + expiryMinutes + " minutes.\n" +
                    "If you didn't request this, you can ignore this email.";

            message.setText(body);
            mailSender.send(message);
            log.info("Verification OTP email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Error sending verification OTP to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send verification OTP");
        }
    }

    public void sendPasswordResetOtp(String toEmail, String otp, long expiryMinutes) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("QuickPick - Password Reset Code");
            String body = "You requested a password reset.\n\n" +
                    "Your password reset code is: " + otp + "\n" +
                    "It expires in " + expiryMinutes + " minutes.\n\n" +
                    "If you did not request this, you can ignore this email.";
            message.setText(body);
            mailSender.send(message);
            log.info("Password reset OTP sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Error sending password reset OTP to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send password reset OTP");
        }
    }
}