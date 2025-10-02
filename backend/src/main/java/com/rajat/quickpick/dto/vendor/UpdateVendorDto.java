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

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVendorDescription() {
        return vendorDescription;
    }

    public void setVendorDescription(String vendorDescription) {
        this.vendorDescription = vendorDescription;
    }

    public List<String> getFoodCategories() {
        return foodCategories;
    }

    public void setFoodCategories(List<String> foodCategories) {
        this.foodCategories = foodCategories;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}