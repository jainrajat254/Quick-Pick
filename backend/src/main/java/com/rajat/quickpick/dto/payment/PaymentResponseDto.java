package com.rajat.quickpick.dto.payment;

import com.rajat.quickpick.enums.PaymentMethod;
import com.rajat.quickpick.enums.PaymentStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PaymentResponseDto {

    private String orderId;
    private String transactionId;
    private String phonepeOrderId;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private Double amount;
    private String responseCode;
    private String message;
    private String paymentUrl;
    private LocalDateTime createdAt;

    private String razorpayKeyId;
}
