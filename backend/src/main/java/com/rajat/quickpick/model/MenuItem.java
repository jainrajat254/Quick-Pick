package com.rajat.quickpick.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "menu_items")
@CompoundIndexes({
    @CompoundIndex(name = "idx_vendorId_isAvailable", def = "{'vendorId': 1, 'isAvailable': 1}"),
    @CompoundIndex(name = "idx_vendorId_category",    def = "{'vendorId': 1, 'category': 1}"),
    @CompoundIndex(name = "idx_vendorId_name",        def = "{'vendorId': 1, 'name': 1}")
})
public class MenuItem {

    @Id
    private String id;

    private String vendorId;

    private String name;
    private String description;

    private double price;

    private String category;

    private boolean isVeg;

    private String imageUrl;

    private boolean isAvailable;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void setIsAvailable(boolean available) {
        isAvailable = available;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

}