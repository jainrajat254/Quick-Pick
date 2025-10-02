package com.rajat.quickpick.dto.order;

import com.rajat.quickpick.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderStatusUpdateDto {

    @NotNull(message = "Order status is required")
    private OrderStatus orderStatus;
}