package com.rajat.quickpick.controller;

import com.rajat.quickpick.dto.payment.PaymentRequestDto;
import com.rajat.quickpick.dto.payment.PaymentResponseDto;
import com.rajat.quickpick.dto.payment.PaymentStatusDto;
import com.rajat.quickpick.security.JwtUtil;
import com.rajat.quickpick.service.RazorpayPaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final RazorpayPaymentService razorpayPaymentService;
    private final JwtUtil jwtUtil;

    @PostMapping("/initiate")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<PaymentResponseDto> initiatePayment(@Valid @RequestBody PaymentRequestDto paymentRequest,
            HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        log.info("Initiating payment for user: {} with order: {}", userId, paymentRequest.getOrderId());

        PaymentResponseDto response = razorpayPaymentService.initiatePayment(userId, paymentRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/callback")
    public ResponseEntity<Map<String, String>> handleRazorpayCallback(HttpServletRequest request) {
        try {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            String payload = sb.toString();
            String signature = request.getHeader("X-Razorpay-Signature");

            log.info("Received Razorpay callback with signature: {}", signature);

            razorpayPaymentService.handleRazorpayWebhook(payload, signature);

            return ResponseEntity.ok(Map.of("status", "success", "message", "Callback processed successfully"));
        } catch (IOException e) {
            log.error("Error reading webhook payload", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status", "error", "message", "Invalid payload"));
        }
    }

    @GetMapping("/status/{orderId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<PaymentStatusDto> checkPaymentStatus(@PathVariable String orderId,
            HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        log.debug("Checking payment status for user: {} with order: {}", userId, orderId);

        PaymentStatusDto status = razorpayPaymentService.checkPaymentStatus(userId, orderId);
        return ResponseEntity.ok(status);
    }

    @PostMapping("/refund/{orderId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Map<String, String>> refundPayment(@PathVariable String orderId,
            @RequestParam(required = false, defaultValue = "User requested refund") String reason,
            HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        log.info("Processing refund for user: {} with order: {} for reason: {}", userId, orderId, reason);

        razorpayPaymentService.refundPayment(orderId, reason);

        return ResponseEntity.ok(Map.of("status", "success", "message", "Refund processed successfully",
                "orderId", orderId));
    }

    private String extractUserIdFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtUtil.extractUserId(token);
        }
        throw new RuntimeException("Invalid token");
    }
}
