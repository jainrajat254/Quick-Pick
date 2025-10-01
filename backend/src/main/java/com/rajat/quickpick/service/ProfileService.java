package com.rajat.quickpick.service;


import com.rajat.quickpick.exception.ResourceNotFoundException;
import com.rajat.quickpick.model.User;
import com.rajat.quickpick.model.Vendor;
import com.rajat.quickpick.model.dto.UserDtos.*;
import com.rajat.quickpick.model.dto.VendorDtos.*;
import com.rajat.quickpick.repository.UserRepository;
import com.rajat.quickpick.repository.VendorRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VendorRepository vendorRepository;

    public UserResponseDto getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserResponseDto response = new UserResponseDto();
        response.setId(user.getId());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setCollegeName(user.getCollegeName());
        response.setDepartment(user.getDepartment());
        response.setProfileImageUrl(user.getProfileImageUrl());
        response.setRole(user.getRole());
        response.setPhoneVerified(user.isPhoneVerified());
        response.setEmailVerified(user.isEmailVerified());

        return response;
    }

    public VendorResponseDto getVendorProfile(String email) {
        Vendor vendor = vendorRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

        VendorResponseDto response = new VendorResponseDto();
        response.setId(vendor.getId());
        response.setVendorName(vendor.getVendorName());
        response.setStoreName(vendor.getStoreName());
        response.setEmail(vendor.getEmail());
        response.setPhone(vendor.getPhone());
        response.setAddress(vendor.getAddress());
        response.setCollegeName(vendor.getCollegeName());
        response.setVendorDescription(vendor.getVendorDescription());
        response.setFoodCategories(vendor.getFoodCategories());
        response.setProfileImageUrl(vendor.getProfileImageUrl());
        response.setRole(vendor.getRole());
        response.setPhoneVerified(vendor.isPhoneVerified());
        response.setEmailVerified(vendor.isEmailVerified());

        return response;
    }

    public UserResponseDto updateUserProfile(String email, UserUpdateDto updateDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (updateDto.getFullName() != null) {
            user.setFullName(updateDto.getFullName());
        }
        if (updateDto.getPhone() != null) {
            user.setPhone(updateDto.getPhone());
        }
        if (updateDto.getDepartment() != null) {
            user.setDepartment(updateDto.getDepartment());
        }
        if (updateDto.getProfileImageUrl() != null) {
            user.setProfileImageUrl(updateDto.getProfileImageUrl());
        }

        user.setUpdatedAt(LocalDateTime.now());
        User updatedUser = userRepository.save(user);

        return getUserProfile(updatedUser.getEmail());
    }

    public VendorResponseDto updateVendorProfile(String email, VendorUpdateDto updateDto) {
        Vendor vendor = vendorRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

        if (updateDto.getVendorName() != null) {
            vendor.setVendorName(updateDto.getVendorName());
        }
        if (updateDto.getStoreName() != null) {
            vendor.setStoreName(updateDto.getStoreName());
        }
        if (updateDto.getPhone() != null) {
            vendor.setPhone(updateDto.getPhone());
        }
        if (updateDto.getAddress() != null) {
            vendor.setAddress(updateDto.getAddress());
        }
        if (updateDto.getVendorDescription() != null) {
            vendor.setVendorDescription(updateDto.getVendorDescription());
        }
        if (updateDto.getFoodCategories() != null) {
            vendor.setFoodCategories(updateDto.getFoodCategories());
        }
        if (updateDto.getProfileImageUrl() != null) {
            vendor.setProfileImageUrl(updateDto.getProfileImageUrl());
        }

        vendor.setUpdatedAt(LocalDateTime.now());
        Vendor updatedVendor = vendorRepository.save(vendor);

        return getVendorProfile(updatedVendor.getEmail());
    }
}