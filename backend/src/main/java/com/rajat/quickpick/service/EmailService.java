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

    public EmailService(@Value("${brevo.api.key}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.brevo.com/v3")
                .defaultHeader("api-key", apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

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



    public void sendEmailVerificationOtp(String toEmail, String otp, Role role, long expires) {
        sendEmail(toEmail,
                "QuickPick - Verification Code",
                "Welcome to QuickPick 🎉<br><br>" +
                        "Your verification code is:<br>" +
                        "<h2 style='color: #4CAF50;'>" + otp + "</h2><br>" +
                        "⏳ This code expires in <b>" + expires + " minutes</b>.<br><br>" +
                        "If you didn't request this, please ignore this email.");
    }

    public void sendPasswordResetOtp(String toEmail, String otp, long expires) {
        sendEmail(toEmail,
                "QuickPick - Password Reset Code",
                "You requested to reset your password.<br><br>" +
                        "Your password reset code is:<br>" +
                        "<h2 style='color: #FF5722;'>" + otp + "</h2><br>" +
                        "⏳ This code expires in <b>" + expires + " minutes</b>.<br><br>" +
                        "If you didn't request this, please ignore this email and your password will remain unchanged.");
    }
}