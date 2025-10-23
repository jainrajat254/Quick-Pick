package com.rajat.quickpick.dto.vendor;

import com.rajat.quickpick.enums.Role;
import lombok.Data;

import java.util.List;

@Data
public class VendorResponseDto {

    private String id;
    private String vendorName;
    private String storeName;
    private String email;
    private String phone;
    private String address;
    private String collegeName;
    private String vendorDescription;
    private List<String> foodCategories;
    private String profileImageUrl;
    private Role role;
    private boolean isPhoneVerified;
    private boolean isEmailVerified;

}