package com.rajat.quickpick.dto.order;

import com.rajat.quickpick.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderNotificationDto {
    private String orderId;
    private String type;
    private String title;
    private String message;
    private OrderStatus orderStatus;
    private double totalAmount;
    private LocalDateTime timestamp;
    private String otp;
}