package com.rajat.quickpick.dto.review;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewResponseDto {

    private String id;
    private String userId;
    private String userName;
    private String vendorId;
    private String orderId;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
}