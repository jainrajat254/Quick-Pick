package com.rajat.quickpick.dto.vendor;

import lombok.Data;

import java.util.List;

@Data
public class UpdateVendorDto {

    private String vendorName;
    private String storeName;
    private String phone;
    private String address;
    private String vendorDescription;
    private List<String> foodCategories;
    private String profileImageUrl;

}