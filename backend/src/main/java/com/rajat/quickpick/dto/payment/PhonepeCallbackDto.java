package com.rajat.quickpick.dto.payment;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PhonepeCallbackDto {
    private String merchantOrderId;
    private String transactionId;
    private Long amount;
    private String state;
    private String responseCode;
    private Object paymentInstrument;
}

