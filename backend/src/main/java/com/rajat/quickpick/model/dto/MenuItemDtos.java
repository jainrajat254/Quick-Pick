package com.rajat.quickpick.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;


public class MenuItemDtos {

    @Data
    public static class MenuItemCreateDto {

        @NotBlank(message = "Menu item name is required")
        private String name;

        private String description;

        @Positive(message = "Price must be positive")
        private double price;

        @PositiveOrZero(message = "Quantity cannot be negative")
        private int quantity;

        @NotBlank(message = "Category is required")
        private String category;

        private boolean isVeg;
        private String imageUrl;
        private boolean isAvailable = true;
    }

    @Data
    public static class MenuItemResponseDto {

        private String id;
        private String vendorId;
        private String name;
        private String description;
        private double price;
        private int quantity;
        private String category;
        private boolean isVeg;
        private String imageUrl;
        private boolean isAvailable;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }


    @Data
    public static class MenuItemUpdateDto {

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

}
