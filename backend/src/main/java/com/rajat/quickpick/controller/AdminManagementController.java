package com.rajat.quickpick.controller;


import com.rajat.quickpick.dto.admin.*;
import com.rajat.quickpick.service.AdminManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin-management")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminManagementController {

    @Autowired
    private AdminManagementService adminManagementService;


    @GetMapping("/users")
    public ResponseEntity<Page<AdminUserDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<AdminUserDto> users = adminManagementService.getAllUsers(page, size);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/college/{collegeName}")
    public ResponseEntity<Page<AdminUserDto>> getUsersByCollege(
            @PathVariable String collegeName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<AdminUserDto> users = adminManagementService.getUsersByCollege(collegeName, page, size);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/users/{userId}/suspend")
    public ResponseEntity<AdminUserDto> suspendUser(@PathVariable String userId,
                                                    @Valid @RequestBody SuspensionDto suspensionDto) {
        AdminUserDto user = adminManagementService.suspendUser(userId, suspensionDto.getReason());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/users/{userId}/unsuspend")
    public ResponseEntity<AdminUserDto> unsuspendUser(@PathVariable String userId) {
        AdminUserDto user = adminManagementService.unsuspendUser(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/vendors")
    public ResponseEntity<Page<AdminVendorDto>> getAllVendors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<AdminVendorDto> vendors = adminManagementService.getAllVendors(page, size);
        return ResponseEntity.ok(vendors);
    }

    @GetMapping("/vendors/pending")
    public ResponseEntity<Page<AdminVendorDto>> getPendingVendors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<AdminVendorDto> vendors = adminManagementService.getPendingVendors(page, size);
        return ResponseEntity.ok(vendors);
    }

    @PostMapping("/vendors/{vendorId}/verify")
    public ResponseEntity<AdminVendorDto> verifyVendor(@PathVariable String vendorId,
                                                       @RequestBody VerificationDto verificationDto) {
        AdminVendorDto vendor = adminManagementService.verifyVendor(vendorId, verificationDto.getNotes());
        return ResponseEntity.ok(vendor);
    }

    @PostMapping("/vendors/{vendorId}/reject")
    public ResponseEntity<AdminVendorDto> rejectVendor(@PathVariable String vendorId,
                                                       @Valid @RequestBody VerificationDto verificationDto) {
        AdminVendorDto vendor = adminManagementService.rejectVendor(vendorId, verificationDto.getRejectionReason());
        return ResponseEntity.ok(vendor);
    }

    @PostMapping("/vendors/{vendorId}/suspend")
    public ResponseEntity<AdminVendorDto> suspendVendor(@PathVariable String vendorId,
                                                        @Valid @RequestBody SuspensionDto suspensionDto) {
        AdminVendorDto vendor = adminManagementService.suspendVendor(vendorId, suspensionDto.getReason());
        return ResponseEntity.ok(vendor);
    }

    @PostMapping("/vendors/{vendorId}/unsuspend")
    public ResponseEntity<AdminVendorDto> unsuspendVendor(@PathVariable String vendorId) {
        AdminVendorDto vendor = adminManagementService.unsuspendVendor(vendorId);
        return ResponseEntity.ok(vendor);
    }

}