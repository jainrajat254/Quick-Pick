package com.rajat.quickpick.dto.menu;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MenuItemResponseDto {

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