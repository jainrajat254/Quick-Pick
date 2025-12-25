package com.rajat.quickpick.dto.order;

import com.rajat.quickpick.enums.OrderStatus;
import com.rajat.quickpick.enums.PaymentMethod;
import com.rajat.quickpick.enums.PaymentStatus;
import com.rajat.quickpick.model.OrderItem;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDto {

    private String id;
    private String userId;
    private String vendorId;
    private String vendorName;
    private String storeName;
    private List<OrderItem> orderItems;
    private double totalAmount;
    private OrderStatus orderStatus;
    private String specialInstructions;

    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private String transactionId;
    private Double amountPaid;
    private LocalDateTime paymentDate;
    private boolean isPaymentAvailable;

    private String otp;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}