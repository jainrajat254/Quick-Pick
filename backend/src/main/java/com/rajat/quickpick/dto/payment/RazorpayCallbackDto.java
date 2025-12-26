package com.rajat.quickpick.dto.payment;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RazorpayCallbackDto {
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
    private Integer amount;
    private String status;
}

