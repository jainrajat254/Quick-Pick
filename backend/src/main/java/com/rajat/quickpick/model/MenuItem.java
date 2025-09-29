package com.rajat.quickpick.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "menu_items")
public class MenuItem {

    @Id
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