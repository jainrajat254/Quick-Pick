package com.rajat.quickpick.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class OrderCreateDto {

    @NotBlank(message = "Vendor ID is required")
    private String vendorId;

    @NotEmpty(message = "Order items cannot be empty")
    private List<OrderItemDto> orderItems;

    private String specialInstructions;
}