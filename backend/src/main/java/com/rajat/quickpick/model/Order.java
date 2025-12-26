package com.rajat.quickpick.model;

import com.rajat.quickpick.enums.OrderStatus;
import com.rajat.quickpick.enums.PaymentMethod;
import com.rajat.quickpick.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    private String userId;
    private String vendorId;

    private List<OrderItem> orderItems;

    private double totalAmount;

    private OrderStatus orderStatus;

    private String specialInstructions;

    private boolean deliveredToVendor = false;
    private LocalDateTime deliveredToVendorAt;

    // Payment-related fields
    private PaymentStatus paymentStatus;  // PENDING, PAID, FAILED, PENDING_ON_DELIVERY
    private PaymentMethod paymentMethod;  // PAY_NOW, PAY_ON_DELIVERY
    private String transactionId;         // PhonePe transaction ID
    private Double amountPaid;            // Amount actually paid
    private LocalDateTime paymentDate;    // When payment was made

    private String otp;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}