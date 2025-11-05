package com.rajat.quickpick.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    private String menuItemId;
    private String menuItemName;
    private String menuItemImage;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    private boolean isAvailable;
    private boolean isVeg;
}

