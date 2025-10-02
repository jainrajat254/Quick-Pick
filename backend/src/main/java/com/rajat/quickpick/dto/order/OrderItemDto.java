package com.rajat.quickpick.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrderItemDto {

    @NotBlank(message = "Menu item ID is required")
    private String menuItemId;

    @Positive(message = "Quantity must be positive")
    private int quantity;
}