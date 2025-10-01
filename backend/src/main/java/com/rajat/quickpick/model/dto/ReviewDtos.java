package com.rajat.quickpick.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

public class ReviewDtos {

    @Data
    public static class ReviewCreateDto {

        @NotBlank(message = "Vendor ID is required")
        private String vendorId;

        @NotBlank(message = "Order ID is required")
        private String orderId;

        @Min(value = 1, message = "Rating must be at least 1")
        @Max(value = 5, message = "Rating cannot exceed 5")
        private int rating;

        private String comment;
    }

    @Data
    public static class ReviewResponseDto {

        private String id;
        private String userId;
        private String userName;
        private String vendorId;
        private String orderId;
        private int rating;
        private String comment;
        private LocalDateTime createdAt;
    }

}
