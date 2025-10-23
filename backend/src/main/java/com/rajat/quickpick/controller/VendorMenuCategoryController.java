package com.rajat.quickpick.controller;


import com.rajat.quickpick.dto.menu.VendorCategoryUpdateDto;
import com.rajat.quickpick.dto.menu.CategoriesResponseDto;
import com.rajat.quickpick.security.JwtUtil;
import com.rajat.quickpick.service.MenuItemService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/vendor-categories")
@RequiredArgsConstructor
public class VendorMenuCategoryController {

    private final MenuItemService vendorMenuCategoryService;
    private final JwtUtil jwtUtil;

    private static final List<String> DEFAULT_CATEGORIES = Arrays.asList(
            "Beverages", "Snacks", "Main Course", "Desserts", "Fast Food",
            "Indian", "Chinese", "South Indian", "North Indian", "Continental",
            "Sandwiches", "Pizza", "Burgers", "Rolls", "Salads", "Juices",
            "Tea/Coffee", "Ice Cream", "Sweets", "Breakfast"
    );


    @GetMapping("/default")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<CategoriesResponseDto> getDefaultCategories() {
        CategoriesResponseDto response = CategoriesResponseDto.builder()
                .categories(DEFAULT_CATEGORIES)
                .count(DEFAULT_CATEGORIES.size())
                .build();
        return ResponseEntity.ok(response);
    }


    @PutMapping
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<CategoriesResponseDto> updateVendorCategories(
            @RequestBody VendorCategoryUpdateDto updateDto,
            HttpServletRequest request) {
        String vendorId = extractUserIdFromToken(request);
        CategoriesResponseDto response = vendorMenuCategoryService.updateVendorCategories(vendorId, updateDto.getCategories());
        return ResponseEntity.ok(response);
    }


    @PostMapping("/reset")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<CategoriesResponseDto> resetVendorCategories(HttpServletRequest request) {
        String vendorId = extractUserIdFromToken(request);
        CategoriesResponseDto response = vendorMenuCategoryService.updateVendorCategories(vendorId, DEFAULT_CATEGORIES);
        return ResponseEntity.ok(response);
    }

    private String extractUserIdFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtUtil.extractUserId(token);
        }
        throw new RuntimeException("Invalid token");
    }
}