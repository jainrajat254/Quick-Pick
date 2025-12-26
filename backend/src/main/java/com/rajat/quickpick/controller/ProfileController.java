package com.rajat.quickpick.controller;


import com.rajat.quickpick.dto.user.UserResponseDto;
import com.rajat.quickpick.dto.user.UpdateUserDto;
import com.rajat.quickpick.dto.vendor.VendorResponseDto;
import com.rajat.quickpick.dto.vendor.UpdateVendorDto;
import com.rajat.quickpick.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private static final Logger log = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    private ProfileService profileService;
    @Autowired
    private com.rajat.quickpick.repository.VendorRepository vendorRepository;
    @Autowired
    private com.rajat.quickpick.repository.PendingVendorRepository pendingVendorRepository;
    @Autowired
    private com.rajat.quickpick.service.EmailService emailService;

    @GetMapping("/user")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> getUserProfile(Authentication authentication) {
        String email = authentication.getName();
        UserResponseDto profile = profileService.getUserProfile(email);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/vendor")
    @PreAuthorize("hasRole('VENDOR') or hasRole('ADMIN')")
    public ResponseEntity<VendorResponseDto> getVendorProfile(Authentication authentication) {
        String email = authentication.getName();
        VendorResponseDto profile = profileService.getVendorProfile(email);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/user")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> updateUserProfile(@Valid @RequestBody UpdateUserDto updateDto,
                                                             Authentication authentication) {
        String email = authentication.getName();
        UserResponseDto updatedProfile = profileService.updateUserProfile(email, updateDto);
        return ResponseEntity.ok(updatedProfile);
    }

    @PutMapping("/vendor")
    @PreAuthorize("hasRole('VENDOR') or hasRole('ADMIN')")
    public ResponseEntity<VendorResponseDto> updateVendorProfile(@Valid @RequestBody UpdateVendorDto updateDto,
                                                                 Authentication authentication) {
        String email = authentication.getName();
        VendorResponseDto updatedProfile = profileService.updateVendorProfile(email, updateDto);
        return ResponseEntity.ok(updatedProfile);
    }

    @GetMapping("/vendor/verification-status")
    @PreAuthorize("hasRole('VENDOR') or hasRole('ADMIN')")
    public ResponseEntity<com.rajat.quickpick.dto.vendor.VerificationStatusDto> getVendorVerificationStatus(Authentication authentication) {
        String email = authentication.getName();
        log.info("verification-status requested for email={}", email);
        var vendorOpt = vendorRepository.findByEmail(email);
        if (vendorOpt.isPresent()) {
            var vendor = vendorOpt.get();
            var status = vendor.getVerificationStatus();
            log.info("vendor found for email={}, verificationStatus={}", email, status);
            if (status == com.rajat.quickpick.enums.VendorVerificationStatus.VERIFIED) {
                return ResponseEntity.ok(new com.rajat.quickpick.dto.vendor.VerificationStatusDto(email, "Verified"));
            } else if (status == com.rajat.quickpick.enums.VendorVerificationStatus.PENDING) {
                return ResponseEntity.ok(new com.rajat.quickpick.dto.vendor.VerificationStatusDto(email, "Pending"));
            } else if (status == com.rajat.quickpick.enums.VendorVerificationStatus.REJECTED) {
                return ResponseEntity.ok(new com.rajat.quickpick.dto.vendor.VerificationStatusDto(email, "Rejected"));
            }
        } else {
            log.info("no vendor record found for email={}", email);
        }
        boolean existsInPending = pendingVendorRepository.findByEmail(email).isPresent();
        log.info("pending vendor exists for email={}: {}", email, existsInPending);
        if (existsInPending) {
            var dto = new com.rajat.quickpick.dto.vendor.VerificationStatusDto(email, "Pending");
            return ResponseEntity.ok(dto);
        }
        var dto = new com.rajat.quickpick.dto.vendor.VerificationStatusDto(email, "Rejected");
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/vendor/request-verification")
    @PreAuthorize("hasRole('VENDOR') or hasRole('ADMIN')")
    public ResponseEntity<java.util.Map<String, String>> requestVendorVerification(Authentication authentication) {
        String email = authentication.getName();
        log.info("vendor request-verification called for email={}", email);
        var vendorOpt = vendorRepository.findByEmail(email);
        if (vendorOpt.isEmpty()) {
            return ResponseEntity.status(404).body(java.util.Map.of("message", "Vendor record not found"));
        }
        var vendor = vendorOpt.get();
        emailService.sendVendorRegistrationNotificationToAdmin(
                vendor.getVendorName(),
                vendor.getEmail(),
                vendor.getStoreName(),
                vendor.getCollegeName()
        );
        return ResponseEntity.ok(java.util.Map.of("message", "Verification request sent to admin"));
    }
}