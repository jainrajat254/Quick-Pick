package com.rajat.quickpick.enums;

public enum PaymentStatus {
    PENDING,                    // Order placed, awaiting payment
    PAID,                       // Payment successful
    PAYMENT_INITIATED,          // PhonePe payment in progress
    PAYMENT_FAILED,             // Payment failed
    PENDING_ON_DELIVERY,        // User chose pay on delivery
    REFUND_INITIATED,           // Refund in progress
    REFUND_COMPLETED            // Refund completed
}

