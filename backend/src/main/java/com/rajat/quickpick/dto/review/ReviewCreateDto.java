package com.rajat.quickpick.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReviewCreateDto {

    @NotBlank(message = "Vendor ID is required")
    private String vendorId;

    @NotBlank(message = "Order ID is required")
    private String orderId;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    private int rating;

    private String comment;
}