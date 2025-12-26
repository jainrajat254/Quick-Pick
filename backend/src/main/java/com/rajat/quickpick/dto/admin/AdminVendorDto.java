package com.rajat.quickpick.dto.admin;

import com.rajat.quickpick.enums.Role;
import com.rajat.quickpick.enums.VendorVerificationStatus;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AdminVendorDto {
    private String id;
    private String vendorName;
    private String storeName;
    private String email;
    private String phone;
    private String address;
    private String collegeName;
    private String vendorDescription;
    private List<String> foodCategories;
    private String gstNumber;
    private String licenseNumber;
    private Role role;
    private boolean emailVerified;
    private boolean phoneVerified;
    private VendorVerificationStatus verificationStatus;
    private LocalDateTime verifiedAt;
    private LocalDateTime rejectedAt;
    private String rejectionReason;
    private String adminNotes;
    private boolean suspended;
    private String suspensionReason;
    private LocalDateTime suspendedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}