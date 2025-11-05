package com.rajat.quickpick.dto.cart;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddToCartDto {

    @NotBlank(message = "Menu item ID is required")
    private String menuItemId;

    @Positive(message = "Quantity must be positive")
    private int quantity = 1;
}

