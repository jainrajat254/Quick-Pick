package com.rajat.quickpick.service;

import com.rajat.quickpick.enums.Role;
import com.rajat.quickpick.enums.VendorVerificationStatus;
import com.rajat.quickpick.exception.BadRequestException;
import com.rajat.quickpick.model.User;
import com.rajat.quickpick.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.rajat.quickpick.dto.admin.*;
import com.rajat.quickpick.dto.vendor.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.rajat.quickpick.model.*;
import com.rajat.quickpick.repository.*;
import com.rajat.quickpick.exception.ResourceNotFoundException;
import java.time.LocalDateTime;
import java.util.List;



@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminManagementService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CollegeRepository collegeRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    public Page<AdminUserDto> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(this::mapToAdminUserDto);
    }

    public Page<AdminUserDto> getUsersByCollege(String collegeName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<User> userPage = userRepository.findByCollegeName(collegeName, pageable);
        return userPage.map(this::mapToAdminUserDto);
    }

    public AdminUserDto suspendUser(String userId, String reason) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() == Role.ADMIN) {
            throw new BadRequestException("Cannot suspend admin user");
        }

        user.setSuspended(true);
        user.setSuspensionReason(reason);
        user.setSuspendedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        log.info("User suspended: {} - Reason: {}", userId, reason);

        return mapToAdminUserDto(savedUser);
    }

    public AdminUserDto unsuspendUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setSuspended(false);
        user.setSuspensionReason(null);
        user.setSuspendedAt(null);
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        log.info("User unsuspended: {}", userId);

        return mapToAdminUserDto(savedUser);
    }

    public Page<AdminVendorDto> getAllVendors(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Vendor> vendorPage = vendorRepository.findAll(pageable);
        return vendorPage.map(this::mapToAdminVendorDto);
    }

    public Page<AdminVendorDto> getPendingVendors(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));
        Page<Vendor> vendorPage = vendorRepository.findByVerificationStatus(VendorVerificationStatus.PENDING, pageable);
        return vendorPage.map(this::mapToAdminVendorDto);
    }
    public AdminVendorDto rejectVendor(String vendorId, String rejectionReason) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

        vendor.setVerificationStatus(VendorVerificationStatus.REJECTED);
        vendor.setRejectedAt(LocalDateTime.now());
        vendor.setRejectionReason(rejectionReason);
        vendor.setUpdatedAt(LocalDateTime.now());

        Vendor savedVendor = vendorRepository.save(vendor);
        log.info("Vendor rejected: {} - Reason: {}", vendorId, rejectionReason);

        return mapToAdminVendorDto(savedVendor);
    }

    public AdminVendorDto verifyVendor(String vendorId, String adminNotes) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

        vendor.setVerificationStatus(VendorVerificationStatus.VERIFIED);
        vendor.setVerifiedAt(LocalDateTime.now());
        vendor.setUpdatedAt(LocalDateTime.now());

        Vendor savedVendor = vendorRepository.save(vendor);
        log.info("Vendor verified: {} - Notes: {}", vendorId, adminNotes);

        return mapToAdminVendorDto(savedVendor);
    }

    public AdminVendorDto suspendVendor(String vendorId, String reason) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

        vendor.setSuspended(true);
        vendor.setSuspensionReason(reason);
        vendor.setSuspendedAt(LocalDateTime.now());
        vendor.setUpdatedAt(LocalDateTime.now());

        Vendor savedVendor = vendorRepository.save(vendor);
        log.info("Vendor suspended: {} - Reason: {}", vendorId, reason);

        return mapToAdminVendorDto(savedVendor);
    }

    public AdminVendorDto unsuspendVendor(String vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

        vendor.setSuspended(false);
        vendor.setSuspensionReason(null);
        vendor.setSuspendedAt(null);
        vendor.setUpdatedAt(LocalDateTime.now());

        Vendor savedVendor = vendorRepository.save(vendor);
        log.info("Vendor unsuspended: {}", vendorId);

        return mapToAdminVendorDto(savedVendor);
    }

    private AdminUserDto mapToAdminUserDto(User user) {
        AdminUserDto dto = new AdminUserDto();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setCollegeName(user.getCollegeName());
        dto.setDepartment(user.getDepartment());
        dto.setStudentId(user.getStudentId());
        dto.setRole(user.getRole());
        dto.setEmailVerified(user.isEmailVerified());
        dto.setPhoneVerified(user.isPhoneVerified());
        dto.setSuspended(user.isSuspended());
        dto.setSuspensionReason(user.getSuspensionReason());
        dto.setSuspendedAt(user.getSuspendedAt());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    private AdminCollegeDto mapToAdminCollegeDto(College college) {
        AdminCollegeDto dto = new AdminCollegeDto();
        dto.setId(college.getId());
        dto.setName(college.getName());
        dto.setAddress(college.getAddress());
        dto.setCity(college.getCity());
        dto.setState(college.getState());
        dto.setTotalUsers(userRepository.countByCollegeName(college.getName()));
        dto.setTotalVendors(vendorRepository.countByCollegeName(college.getName()));
        dto.setCreatedAt(college.getCreatedAt());
        return dto;
    }

    private AdminVendorDto mapToAdminVendorDto(Vendor vendor) {
        AdminVendorDto dto = new AdminVendorDto();
        dto.setId(vendor.getId());
        dto.setVendorName(vendor.getVendorName());
        dto.setStoreName(vendor.getStoreName());
        dto.setEmail(vendor.getEmail());
        dto.setPhone(vendor.getPhone());
        dto.setAddress(vendor.getAddress());
        dto.setCollegeName(vendor.getCollegeName());
        dto.setVendorDescription(vendor.getVendorDescription());
        dto.setFoodCategories(vendor.getFoodCategories());
        dto.setGstNumber(vendor.getGstNumber());
        dto.setLicenseNumber(vendor.getLicenseNumber());
        dto.setFoodLicenseNumber(vendor.getFoodLicenseNumber());
        dto.setRole(vendor.getRole());
        dto.setEmailVerified(vendor.isEmailVerified());
        dto.setPhoneVerified(vendor.isPhoneVerified());
        dto.setVerificationStatus(vendor.getVerificationStatus());
        dto.setVerifiedAt(vendor.getVerifiedAt());
        dto.setRejectedAt(vendor.getRejectedAt());
        dto.setRejectionReason(vendor.getRejectionReason());
        dto.setSuspended(vendor.isSuspended());
        dto.setSuspensionReason(vendor.getSuspensionReason());
        dto.setSuspendedAt(vendor.getSuspendedAt());
        dto.setCreatedAt(vendor.getCreatedAt());
        dto.setUpdatedAt(vendor.getUpdatedAt());
        return dto;
    }
}
