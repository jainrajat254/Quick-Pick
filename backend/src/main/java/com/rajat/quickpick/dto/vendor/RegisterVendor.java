package com.rajat.quickpick.dto.vendor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class RegisterVendor {

    @NotBlank(message = "Vendor name is required")
    private String vendorName;

    @NotBlank(message = "Store name is required")
    private String storeName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 10, message = "Phone number must be 10 digits")
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "College name is required")
    private String collegeName;

    @NotBlank(message = "Vendor description is required")
    private String vendorDescription;

    private List<String> foodCategories;

    @NotBlank(message = "GST number is required")
    private String gstNumber;

    @NotBlank(message = "License number is required")
    private String licenseNumber;

    @NotBlank(message = "Food license number is required")
    private String foodLicenseNumber;


}