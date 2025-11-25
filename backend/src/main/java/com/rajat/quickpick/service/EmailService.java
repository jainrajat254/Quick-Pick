package com.rajat.quickpick.service;

import com.rajat.quickpick.enums.Role;
import com.rajat.quickpick.utils.Secrets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@Slf4j
public class EmailService {

    private final WebClient webClient;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name:QuickPick Team}")
    private String senderName;

    @Value("${app.baseUrl}")
    private String baseUrl;

    // ---- Constructor for Spring (Fix for error) ----
    public EmailService(@Value("${brevo.api.key}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.brevo.com/v3")
                .defaultHeader("api-key", apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    // ---- Generic Email Sender ----
    private void sendEmail(String to, String subject, String textBody) {
        Map<String, Object> requestBody = Map.of(
                "sender", Map.of("email", senderEmail, "name", senderName),
                "to", new Object[]{Map.of("email", to)},
                "subject", subject,
                "htmlContent", "<p>" + textBody.replace("\n", "<br>") + "</p>"
        );

        try {
            String response = webClient.post()
                    .uri("/smtp/email")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("📩 Email sent successfully to {} | Response: {}", to, response);
        } catch (Exception e) {
            log.error("❌ Failed to send email to {} | Error: {}", to, e.getMessage());
            throw new RuntimeException("Email sending failed", e);
        }
    }

    // ---- Email Types ----
    public void sendVerificationEmail(String toEmail, String token, Role role) {
        String link = baseUrl + "/api/auth/verify-email?token=" + token + "&type=" + role;
        long hours = Secrets.EMAIL_VERIFICATION_TOKEN_EXPIRATION / (1000 * 60 * 60);

        sendEmail(toEmail,
                "QuickPick - Email Verification",
                "Welcome to QuickPick 🎉<br><br>" +
                        "Click to verify your account:<br>" +
                        "<a href=\"" + link + "\">Verify Email</a><br><br>" +
                        "⏳ Expires in <b>" + hours + " hours</b>.");
    }

    public void sendPasswordResetEmail(String toEmail, String token, Role role) {
        String link = baseUrl + "/api/auth/reset-password?token=" + token + "&type=" + role;
        long hours = Secrets.PASSWORD_RESET_TOKEN_EXPIRATION / (1000 * 60 * 60);

        sendEmail(toEmail,
                "QuickPick - Reset Password",
                "Reset your password:<br>" +
                        "<a href=\"" + link + "\">Reset Now</a><br><br>" +
                        "⏳ Expires in: <b>" + hours + " hours</b>.");
    }

    public void sendEmailVerificationOtp(String toEmail, String otp, Role role, long expires) {
        sendEmail(toEmail,
                "QuickPick Verification Code",
                "Your OTP:<br><h2>" + otp + "</h2><br>Expires in <b>" + expires + " minutes</b>.");
    }

    public void sendPasswordResetOtp(String toEmail, String otp, long expires) {
        sendEmail(toEmail,
                "QuickPick Reset Code",
                "Your reset OTP:<br><h2>" + otp + "</h2><br>Expires in <b>" + expires + "minutes</b>.");
    }
}