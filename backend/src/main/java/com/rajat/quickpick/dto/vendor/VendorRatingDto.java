package com.rajat.quickpick.dto.vendor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorRatingDto {
    private String vendorId;
    private Double averageRating;
    private Integer totalReviews;
    private Map<Integer, Long> ratingDistribution;
}
