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


@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    @Autowired
    private ProfileService profileService;

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
}