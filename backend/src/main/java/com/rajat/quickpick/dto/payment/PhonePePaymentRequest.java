package com.rajat.quickpick.dto.payment;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class PhonePePaymentRequest {
    private String merchantOrderId;
    private long amount;
    private String redirectUrl;
    private String callbackUrl;
    private Map<String, Object> paymentInstrument;
}

