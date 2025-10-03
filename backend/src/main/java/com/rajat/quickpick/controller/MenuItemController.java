package com.rajat.quickpick.controller;

import com.rajat.quickpick.dto.menu.MenuItemCreateDto;
import com.rajat.quickpick.dto.menu.MenuItemResponseDto;
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
    public ResponseEntity<List<MenuItemResponseDto>> getMyAvailableMenuItems(HttpServletRequest request) {
        String vendorId = extractUserIdFromToken(request);
        List<MenuItemResponseDto> menuItems = menuItemService.getAvailableMenuItemsByVendor(vendorId);
        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("/my-menu/category/{category}")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<List<MenuItemResponseDto>> getMyMenuItemsByCategory(@PathVariable String category,
                                                                              HttpServletRequest request) {
        String vendorId = extractUserIdFromToken(request);
        List<MenuItemResponseDto> menuItems = menuItemService.getMenuItemsByCategory(vendorId, category);
        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("/my-menu/search")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<List<MenuItemResponseDto>> searchMyMenuItems(@RequestParam String query,
                                                                       HttpServletRequest request) {
        String vendorId = extractUserIdFromToken(request);
        List<MenuItemResponseDto> menuItems = menuItemService.searchMenuItems(vendorId, query);
        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("/my-menu/price-range")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<List<MenuItemResponseDto>> getMyMenuItemsByPriceRange(
            @RequestParam double minPrice,
            @RequestParam double maxPrice,
            HttpServletRequest request) {
        String vendorId = extractUserIdFromToken(request);
        List<MenuItemResponseDto> menuItems = menuItemService.getMenuItemsByPriceRange(vendorId, minPrice, maxPrice);
        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("/my-menu/categories")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<List<String>> getMyMenuCategories(HttpServletRequest request) {
        String vendorId = extractUserIdFromToken(request);
        List<String> categories = menuItemService.getMenuCategories(vendorId);
        return ResponseEntity.ok(categories);
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
    public ResponseEntity<List<MenuItemResponseDto>> getVendorMenu(@PathVariable String vendorId) {
        List<MenuItemResponseDto> menuItems = menuItemService.getPublicMenuByVendor(vendorId);
        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("/vendor/{vendorId}/category/{category}")
    public ResponseEntity<List<MenuItemResponseDto>> getVendorMenuByCategory(@PathVariable String vendorId,
                                                                             @PathVariable String category) {
        List<MenuItemResponseDto> menuItems = menuItemService.getPublicMenuByCategory(vendorId, category);
        return ResponseEntity.ok(menuItems);
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