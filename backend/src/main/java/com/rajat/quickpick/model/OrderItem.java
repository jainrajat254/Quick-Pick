package com.rajat.quickpick.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    private String menuItemId;
    private String menuItemName;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
}