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

    @Value("${app.admin.email}")
    private String adminEmail;

    public EmailService(@Value("${brevo.api.key}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.brevo.com/v3")
                .defaultHeader("api-key", apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    private void sendEmail(String to, String subject, String htmlContent) {
        Map<String, Object> requestBody = Map.of(
                "sender", Map.of("email", senderEmail, "name", senderName),
                "to", new Object[]{Map.of("email", to)},
                "subject", subject,
                "htmlContent", htmlContent
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

    private String getEmailTemplate(String title, String heading, String content, String otpCode, String footer) {
        return "<!DOCTYPE html>" +
                "<html lang='en'>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>" + title + "</title>" +
                "</head>" +
                "<body style='margin:0;padding:0;font-family:-apple-system,BlinkMacSystemFont,\"Segoe UI\",Roboto,\"Helvetica Neue\",Arial,sans-serif;background-color:#f5f5f5;'>" +
                "<table width='100%' cellpadding='0' cellspacing='0' style='background-color:#f5f5f5;padding:40px 20px;'>" +
                "<tr><td align='center'>" +
                "<table width='600' cellpadding='0' cellspacing='0' style='background-color:#ffffff;border-radius:12px;box-shadow:0 4px 6px rgba(0,0,0,0.1);overflow:hidden;max-width:100%;'>" +
                "<tr>" +
                "<td style='background:linear-gradient(135deg,#667eea 0%,#764ba2 100%);padding:40px 30px;text-align:center;'>" +
                "<h1 style='margin:0;color:#ffffff;font-size:32px;font-weight:700;letter-spacing:-0.5px;'>🍕 QuickPick</h1>" +
                "<p style='margin:8px 0 0;color:#ffffff;font-size:16px;opacity:0.95;'>Your Campus Food Delivery</p>" +
                "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style='padding:40px 30px;'>" +
                "<h2 style='margin:0 0 20px;color:#1a1a1a;font-size:24px;font-weight:600;'>" + heading + "</h2>" +
                "<p style='margin:0 0 30px;color:#4a5568;font-size:16px;line-height:1.6;'>" + content + "</p>" +
                (otpCode != null ?
                        "<div style='background:linear-gradient(135deg,#f093fb 0%,#f5576c 100%);border-radius:10px;padding:25px;text-align:center;margin:30px 0;'>" +
                                "<p style='margin:0 0 10px;color:#ffffff;font-size:14px;font-weight:500;text-transform:uppercase;letter-spacing:1px;'>Your Verification Code</p>" +
                                "<div style='background-color:rgba(255,255,255,0.95);border-radius:8px;padding:20px;display:inline-block;'>" +
                                "<p style='margin:0;font-size:36px;font-weight:700;color:#667eea;letter-spacing:8px;font-family:\"Courier New\",monospace;'>" + otpCode + "</p>" +
                                "</div>" +
                                "</div>" : "") +
                "<p style='margin:30px 0 0;color:#718096;font-size:14px;line-height:1.6;'>" + footer + "</p>" +
                "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style='background-color:#f7fafc;padding:30px;text-align:center;border-top:1px solid #e2e8f0;'>" +
                "<p style='margin:0 0 10px;color:#a0aec0;font-size:14px;'>© 2024 QuickPick. All rights reserved.</p>" +
                "<p style='margin:0;color:#a0aec0;font-size:12px;'>Fast, Fresh & Delivered to Your Campus</p>" +
                "<div style='margin-top:20px;'>" +
                "<a href='#' style='display:inline-block;margin:0 10px;color:#667eea;text-decoration:none;font-size:12px;'>Help Center</a>" +
                "<span style='color:#cbd5e0;'>•</span>" +
                "<a href='#' style='display:inline-block;margin:0 10px;color:#667eea;text-decoration:none;font-size:12px;'>Privacy Policy</a>" +
                "<span style='color:#cbd5e0;'>•</span>" +
                "<a href='#' style='display:inline-block;margin:0 10px;color:#667eea;text-decoration:none;font-size:12px;'>Terms of Service</a>" +
                "</div>" +
                "</td>" +
                "</tr>" +
                "</table>" +
                "</td></tr>" +
                "</table>" +
                "</body>" +
                "</html>";
    }

    public void sendEmailVerificationOtp(String toEmail, String otp, Role role, long expires) {
        String userType = role == Role.STUDENT ? "Student" : "Vendor";
        String heading = "Welcome to QuickPick! 🎉";
        String content = "Thank you for registering as a " + userType + ". To complete your registration and start enjoying delicious food delivery on campus, please verify your email address using the code below.";
        String footer = "⏱️ <strong>This code will expire in " + expires + " minutes.</strong><br><br>" +
                "If you didn't create an account with QuickPick, you can safely ignore this email.";

        String htmlContent = getEmailTemplate(
                "Verify Your Email - QuickPick",
                heading,
                content,
                otp,
                footer
        );

        sendEmail(toEmail, "🍕 QuickPick - Verify Your Email", htmlContent);
    }

    public void sendPasswordResetOtp(String toEmail, String otp, long expires) {
        String heading = "Password Reset Request 🔐";
        String content = "We received a request to reset your password. Use the verification code below to set a new password for your QuickPick account.";
        String footer = "⏱️ <strong>This code will expire in " + expires + " minutes.</strong><br><br>" +
                "If you didn't request a password reset, please ignore this email and your password will remain unchanged. Your account is secure.";

        String htmlContent = getEmailTemplate(
                "Reset Your Password - QuickPick",
                heading,
                content,
                otp,
                footer
        );

        sendEmail(toEmail, "🔐 QuickPick - Password Reset Code", htmlContent);
    }

    public void sendVendorRegistrationNotificationToAdmin(String vendorName, String vendorEmail, String storeName, String collegeName) {
        String heading = "New Vendor Registration 🏪";
        String content = "A new vendor has registered on QuickPick and is awaiting verification.<br><br>" +
                "<strong>Vendor Details:</strong><br>" +
                "• <strong>Vendor Name:</strong> " + vendorName + "<br>" +
                "• <strong>Store Name:</strong> " + storeName + "<br>" +
                "• <strong>Email:</strong> " + vendorEmail + "<br>" +
                "• <strong>College:</strong> " + collegeName + "<br><br>" +
                "Please review and verify the vendor application in the admin panel.";
        String footer = "This is an automated notification. Please log in to the admin dashboard to review the vendor's details and documentation.";

        String htmlContent = getEmailTemplate(
                "New Vendor Registration - QuickPick Admin",
                heading,
                content,
                null,
                footer
        );

        sendEmail(adminEmail, "🏪 QuickPick - New Vendor Registration Pending", htmlContent);
    }

    public void sendVendorApprovalNotification(String vendorEmail, String vendorName, String storeName) {
        String heading = "Congratulations! Your Vendor Account is Approved ✅";
        String content = "Great news, " + vendorName + "! Your vendor application for <strong>" + storeName + "</strong> has been reviewed and approved by our admin team.<br><br>" +
                "You can now start adding menu items and accepting orders from students on campus. Welcome to the QuickPick family!<br><br>" +
                "<strong>Next Steps:</strong><br>" +
                "• Log in to your vendor dashboard<br>" +
                "• Set up your menu and pricing<br>" +
                "• Configure your store timings<br>" +
                "• Start receiving orders!";
        String footer = "If you have any questions or need assistance setting up your store, please don't hesitate to contact our support team.";

        String htmlContent = getEmailTemplate(
                "Vendor Account Approved - QuickPick",
                heading,
                content,
                null,
                footer
        );

        sendEmail(vendorEmail, "✅ QuickPick - Your Vendor Account is Approved!", htmlContent);
    }

    public void sendVendorRejectionNotification(String vendorEmail, String vendorName, String storeName, String rejectionReason) {
        String heading = "Vendor Application Update ❌";
        String content = "Dear " + vendorName + ",<br><br>" +
                "Thank you for your interest in joining QuickPick as a vendor. After careful review, we regret to inform you that your application for <strong>" + storeName + "</strong> has not been approved at this time.<br><br>" +
                "<strong>Reason:</strong><br>" +
                rejectionReason + "<br><br>" +
                "We encourage you to review our vendor requirements and reapply in the future. You may register again after addressing the mentioned concerns.";
        String footer = "If you have any questions regarding this decision or need clarification, please feel free to contact our support team.";

        String htmlContent = getEmailTemplate(
                "Vendor Application Status - QuickPick",
                heading,
                content,
                null,
                footer
        );

        sendEmail(vendorEmail, "QuickPick - Vendor Application Update", htmlContent);
    }
}