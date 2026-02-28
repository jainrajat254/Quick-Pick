package com.rajat.quickpick.dto.menu;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class UpdateMenuItemDto {

    private String name;
    private String description;

    @PositiveOrZero(message = "Price cannot be negative")
    private Double price;

    private String category;
    @JsonProperty("isVeg")
    private Boolean veg;
    private String imageUrl;
    private Boolean isAvailable;
}