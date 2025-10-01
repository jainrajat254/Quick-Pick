package com.rajat.quickpick.model.dto;

import com.rajat.quickpick.model.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.time.LocalDateTime;
import com.rajat.quickpick.model.OrderItem;

public class OrderDtos {

    @Data
    public static class OrderCreateDto {

        @NotBlank(message = "Vendor ID is required")
        private String vendorId;

        @NotEmpty(message = "Order items cannot be empty")
        private List<OrderItemDto> orderItems;

        private String specialInstructions;
    }

    @Data
    public static class OrderItemDto {

        @NotBlank(message = "Menu item ID is required")
        private String menuItemId;

        @Positive(message = "Quantity must be positive")
        private int quantity;
    }

    @Data
    public static class OrderResponseDto {

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

    @Data
    public static class OrderStatusUpdateDto {

        @NotNull(message = "Order status is required")
        private OrderStatus orderStatus;
    }

}

