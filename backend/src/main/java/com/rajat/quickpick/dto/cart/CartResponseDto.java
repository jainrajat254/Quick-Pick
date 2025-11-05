package com.rajat.quickpick.dto.cart;

import com.rajat.quickpick.model.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponseDto {

    private String id;
    private String userId;
    private String vendorId;
    private String vendorName;
    private List<CartItem> items;
    private double totalAmount;
    private int itemCount;
    private String createdAt;
    private String updatedAt;
}
