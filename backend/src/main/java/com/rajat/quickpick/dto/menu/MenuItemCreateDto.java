package com.rajat.quickpick.dto.menu;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class MenuItemCreateDto {

    @NotBlank(message = "Menu item name is required")
    private String name;

    private String description;

    @Positive(message = "Price must be positive")
    private double price;

    @PositiveOrZero(message = "Quantity cannot be negative")
    private int quantity;

    @NotBlank(message = "Category is required")
    private String category;

    @JsonProperty("isVeg")
    private boolean veg;
    private String imageUrl;
    private boolean isAvailable = true;
}