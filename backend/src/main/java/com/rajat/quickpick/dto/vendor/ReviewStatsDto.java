package com.rajat.quickpick.dto.vendor;

import lombok.Data;

@Data
public class ReviewStatsDto {

    private String vendorId;
    private Double averageRating;
    private Integer totalReviews;
    private Integer oneStarCount;
    private Integer twoStarCount;
    private Integer threeStarCount;
    private Integer fourStarCount;
    private Integer fiveStarCount;
    private Double oneStarPercentage;
    private Double twoStarPercentage;
    private Double threeStarPercentage;
    private Double fourStarPercentage;
    private Double fiveStarPercentage;

}
