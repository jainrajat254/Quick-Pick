package com.rajat.quickpick.dto.menu;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class MenuItemUpdateDto {

    private String name;
    private String description;

    @PositiveOrZero(message = "Price cannot be negative")
    private Double price;

    @PositiveOrZero(message = "Quantity cannot be negative")
    private Integer quantity;

    private String category;
    private Boolean isVeg;
    private String imageUrl;
    private Boolean isAvailable;
}