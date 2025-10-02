package com.rajat.quickpick.dto.order;

import com.rajat.quickpick.model.OrderItem;
import com.rajat.quickpick.enums.OrderStatus;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}