package com.rajat.quickpick.controller;

import com.rajat.quickpick.dto.menu.MenuItemCreateDto;
import com.rajat.quickpick.dto.menu.MenuItemResponseDto;
import com.rajat.quickpick.dto.menu.MenuItemsResponseDto;
import com.rajat.quickpick.dto.menu.CategoriesResponseDto;
import com.rajat.quickpick.dto.menu.UpdateMenuItemDto;
import com.rajat.quickpick.security.JwtUtil;
import com.rajat.quickpick.service.MenuItemService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;
    private final JwtUtil jwtUtil;

    @PostMapping
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<MenuItemResponseDto> createMenuItem(@Valid @RequestBody MenuItemCreateDto createDto,
                                                              HttpServletRequest request) {
        String vendorId = extractUserIdFromToken(request);
        MenuItemResponseDto menuItem = menuItemService.createMenuItem(vendorId, createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(menuItem);
    }

    @GetMapping("/my-menu")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<Page<MenuItemResponseDto>> getMyMenuItemsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest request) {
        String vendorId = extractUserIdFromToken(request);
        Page<MenuItemResponseDto> menuItems = menuItemService.getMenuItemsByVendor(vendorId, page, size);
        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("/my-menu/available")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<MenuItemsResponseDto> getMyAvailableMenuItems(HttpServletRequest request) {
        String vendorId = extractUserIdFromToken(request);
        MenuItemsResponseDto response = menuItemService.getAvailableMenuItemsByVendor(vendorId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-menu/category/{category}")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<MenuItemsResponseDto> getMyMenuItemsByCategory(@PathVariable String category,
                                                                              HttpServletRequest request) {
        String vendorId = extractUserIdFromToken(request);
        MenuItemsResponseDto response = menuItemService.getMenuItemsByCategory(vendorId, category);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-menu/search")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<MenuItemsResponseDto> searchMyMenuItems(@RequestParam String query,
                                                                       HttpServletRequest request) {
        String vendorId = extractUserIdFromToken(request);
        MenuItemsResponseDto response = menuItemService.searchMenuItems(vendorId, query);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-menu/price-range")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<MenuItemsResponseDto> getMyMenuItemsByPriceRange(
            @RequestParam double minPrice,
            @RequestParam double maxPrice,
            HttpServletRequest request) {
        String vendorId = extractUserIdFromToken(request);
        MenuItemsResponseDto response = menuItemService.getMenuItemsByPriceRange(vendorId, minPrice, maxPrice);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-menu/categories")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<CategoriesResponseDto> getMyMenuCategories(HttpServletRequest request) {
        String vendorId = extractUserIdFromToken(request);
        CategoriesResponseDto response = menuItemService.getMenuCategories(vendorId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{menuItemId}")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<MenuItemResponseDto> updateMenuItem(@PathVariable String menuItemId,
                                                              @Valid @RequestBody UpdateMenuItemDto updateDto,
                                                              HttpServletRequest request) {
        String vendorId = extractUserIdFromToken(request);
        MenuItemResponseDto updatedMenuItem = menuItemService.updateMenuItem(vendorId, menuItemId, updateDto);
        return ResponseEntity.ok(updatedMenuItem);
    }

    @PatchMapping("/{menuItemId}/toggle-availability")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<MenuItemResponseDto> toggleAvailability(@PathVariable String menuItemId,
                                                                  HttpServletRequest request) {
        String vendorId = extractUserIdFromToken(request);
        MenuItemResponseDto updatedMenuItem = menuItemService.toggleAvailability(vendorId, menuItemId);
        return ResponseEntity.ok(updatedMenuItem);
    }

    @PatchMapping("/{menuItemId}/quantity")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<MenuItemResponseDto> updateQuantity(@PathVariable String menuItemId,
                                                              @RequestParam int quantity,
                                                              HttpServletRequest request) {
        String vendorId = extractUserIdFromToken(request);
        MenuItemResponseDto updatedMenuItem = menuItemService.updateQuantity(vendorId, menuItemId, quantity);
        return ResponseEntity.ok(updatedMenuItem);
    }

    @DeleteMapping("/{menuItemId}")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<Map<String, String>> deleteMenuItem(@PathVariable String menuItemId,
                                                              HttpServletRequest request) {
        String vendorId = extractUserIdFromToken(request);
        menuItemService.deleteMenuItem(vendorId, menuItemId);
        return ResponseEntity.ok(Map.of("message", "Menu item deleted successfully"));
    }

    @DeleteMapping("/bulk")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<Map<String, String>> deleteMultipleMenuItems(@RequestBody Map<String, List<String>> request,
                                                                       HttpServletRequest httpRequest) {
        String vendorId = extractUserIdFromToken(httpRequest);
        List<String> menuItemIds = request.get("menuItemIds");
        menuItemService.deleteMultipleMenuItems(vendorId, menuItemIds);
        return ResponseEntity.ok(Map.of("message", "Menu items deleted successfully"));
    }

    @GetMapping("/my-menu/stats")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<Map<String, Long>> getMenuItemStats(HttpServletRequest request) {
        String vendorId = extractUserIdFromToken(request);
        Map<String, Long> stats = menuItemService.getMenuItemStats(vendorId);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<MenuItemsResponseDto> getVendorMenu(@PathVariable String vendorId) {
        MenuItemsResponseDto response = menuItemService.getPublicMenuByVendor(vendorId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vendor/{vendorId}/category/{category}")
    public ResponseEntity<MenuItemsResponseDto> getVendorMenuByCategory(@PathVariable String vendorId,
                                                                             @PathVariable String category) {
        MenuItemsResponseDto response = menuItemService.getPublicMenuByCategory(vendorId, category);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{menuItemId}")
    public ResponseEntity<MenuItemResponseDto> getMenuItemById(@PathVariable String menuItemId) {
        MenuItemResponseDto menuItem = menuItemService.getMenuItemById(menuItemId);
        return ResponseEntity.ok(menuItem);
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