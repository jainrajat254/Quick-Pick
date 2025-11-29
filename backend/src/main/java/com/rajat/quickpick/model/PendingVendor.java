package com.rajat.quickpick.model;

import com.rajat.quickpick.enums.Role;
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
@Document(collection = "pending_vendors")
public class PendingVendor {

    @Id
    private String id;

    private String vendorName;
    private String storeName;
    private String email;
    private String phone;
    private String address;
    private String password;
    private List<String> foodCategories;
    private String collegeName;
    private String vendorDescription;
    private String gstNumber;
    private String licenseNumber;
    private String foodLicenseNumber;

    private Role role = Role.VENDOR;

    private String otp;
    private LocalDateTime otpCreatedAt;
    private LocalDateTime otpExpiresAt;
    private Integer otpAttempts;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

