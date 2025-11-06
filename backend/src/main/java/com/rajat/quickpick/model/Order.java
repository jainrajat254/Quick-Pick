package com.rajat.quickpick.model;

import com.rajat.quickpick.enums.OrderStatus;
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

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private boolean deliveredToVendor = false;
    private LocalDateTime deliveredToVendorAt;
}