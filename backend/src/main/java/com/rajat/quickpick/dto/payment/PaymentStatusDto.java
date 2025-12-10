package com.rajat.quickpick.dto.payment;

import com.rajat.quickpick.enums.PaymentMethod;
import com.rajat.quickpick.enums.PaymentStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentStatusDto {

    private String orderId;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private String transactionId;
    private Double amountPaid;
    private String message;
    private boolean isPaymentAvailable;
}

