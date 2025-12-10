package com.rajat.quickpick.dto.payment;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PhonePePaymentResponse {
    private boolean success;
    private ResponseData data;

    @Data
    @NoArgsConstructor
    public static class ResponseData {
        private String redirectUrl;
        private String merchantOrderId;
        private String state;
    }
}

