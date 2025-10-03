package com.rajat.quickpick.dto.admin;

import lombok.Data;

@Data
public class VerificationDto {
    private String notes = "Verified by admin";
    private String rejectionReason = "Rejected by admin";
}