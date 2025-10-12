package com.rajat.quickpick.controller;

import com.rajat.quickpick.model.MenuItem;
import com.rajat.quickpick.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import com.rajat.quickpick.dto.vendor.*;
import com.rajat.quickpick.dto.menu.*;
import com.rajat.quickpick.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    @Autowired
    private SearchService searchService;

    @Autowired
    private JwtUtil jwtUtil;

    private String extractUserIdFromRequest(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        return jwtUtil.extractUserId(token);
    }

    @GetMapping("/vendors")
    public ResponseEntity<List<VendorResponseDto>> searchVendors(
            @RequestParam(required = false) String query,
            HttpServletRequest request) {

        String userId = extractUserIdFromRequest(request);
        List<VendorResponseDto> result = searchService.searchVendors(userId, query);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/vendors/college")
    public ResponseEntity<List<VendorResponseDto>> getAllVendorsInCollege(
            HttpServletRequest request) {

        String userId = extractUserIdFromRequest(request);
        List<VendorResponseDto> result = searchService.searchVendors(userId, null);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/vendors/{vendorId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('VENDOR') or hasRole('ADMIN')")
    public ResponseEntity<VendorResponseDto> getVendorById(@PathVariable String vendorId) {
        VendorResponseDto vendor = searchService.getVendorById(vendorId);
        if (vendor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vendor);
    }


    @GetMapping("/menu-items")
    public ResponseEntity<Page<MenuItemResponseDto>> searchMenuItems(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String vendorId,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "true") boolean availableOnly,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest request) {

        String userId = extractUserIdFromRequest(request);

        Pageable pageable = PageRequest.of(page, size);

        MenuItemSearchDto criteria = MenuItemSearchDto.builder()
                .searchQuery(query)
                .vendorId(vendorId)
                .category(category)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .availableOnly(availableOnly)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .page(page)
                .size(size)
                .build();

        Page<MenuItemResponseDto> result = searchService.searchMenuItems(userId, criteria, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/menu-items/strict-search")
    public ResponseEntity<Page<MenuItemResponseDto>> strictSearchMenuItems(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String vendorId,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "true") boolean availableOnly,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest request) {

        String userId = extractUserIdFromRequest(request);

        Pageable pageable = PageRequest.of(page, size);

        MenuItemSearchDto criteria = MenuItemSearchDto.builder()
                .searchQuery(query)
                .vendorId(vendorId)
                .category(category)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .availableOnly(availableOnly)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .page(page)
                .size(size)
                .build();

        Page<MenuItemResponseDto> result = searchService.strictSearchMenuItems(userId, criteria, pageable);
        return ResponseEntity.ok(result);
    }

}