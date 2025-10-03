package com.rajat.quickpick.model;

import com.rajat.quickpick.enums.Role;
import com.rajat.quickpick.enums.VendorVerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "vendors")
public class Vendor {

    @Id
    private String id;

    private String vendorName;
    private String storeName;
    private String email;
    private String phone;
    private String address;
    private String profileImageUrl;

    private boolean isPhoneVerified;
    private boolean isEmailVerified;

    private String password;

    private List<String> foodCategories;

    private String collegeName;

    private String vendorDescription;

    private String gstNumber;
    private String licenseNumber;
    private String foodLicenseNumber;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Role role = Role.VENDOR;

    private VendorVerificationStatus verificationStatus = VendorVerificationStatus.PENDING;
    private LocalDateTime verifiedAt;
    private LocalDateTime rejectedAt;
    private String rejectionReason;
    private boolean suspended = false;
    private String suspensionReason;
    private LocalDateTime suspendedAt;

}