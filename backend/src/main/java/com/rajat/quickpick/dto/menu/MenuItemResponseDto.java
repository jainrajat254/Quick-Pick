package com.rajat.quickpick.dto.menu;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("isVeg")
    private boolean veg;
    private String imageUrl;
     @JsonProperty("isAvailable")
    private boolean isAvailable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}