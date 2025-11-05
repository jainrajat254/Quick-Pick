package com.rajat.quickpick.dto.cart;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateCartItemDto {

    @Positive(message = "Quantity must be positive")
    private int quantity;
}

