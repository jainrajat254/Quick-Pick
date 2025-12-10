package com.rajat.quickpick.dto.payment;

import com.rajat.quickpick.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentRequestDto {

    @NotBlank(message = "Order ID is required")
    private String orderId;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    private String userPhoneNumber;

    private String redirectUrl;
}

