package com.rajat.quickpick.service;

import com.rajat.quickpick.dto.menu.MenuItemCreateDto;
import com.rajat.quickpick.dto.menu.MenuItemResponseDto;
import com.rajat.quickpick.dto.menu.MenuItemsResponseDto;
import com.rajat.quickpick.dto.menu.CategoriesResponseDto;
import com.rajat.quickpick.dto.menu.UpdateMenuItemDto;
import com.rajat.quickpick.enums.VendorVerificationStatus;
import com.rajat.quickpick.exception.BadRequestException;
import com.rajat.quickpick.exception.ResourceNotFoundException;
import com.rajat.quickpick.model.MenuItem;
import com.rajat.quickpick.model.Vendor;
import com.rajat.quickpick.repository.MenuItemRepository;
import com.rajat.quickpick.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private VendorRepository vendorRepository;

    private static final Logger log = LoggerFactory.getLogger(AdminManagementService.class);

    private Vendor validateVendor(String vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

        if (vendor.isSuspended()) {
            throw new BadRequestException("Your account is suspended");
        }

        if (vendor.getVerificationStatus() != VendorVerificationStatus.VERIFIED) {
            throw new BadRequestException("Your account is not verified yet");
        }

        return vendor;
    }

    private MenuItemResponseDto mapToResponseDto(MenuItem menuItem) {
        MenuItemResponseDto dto = new MenuItemResponseDto();
        dto.setId(menuItem.getId());
        dto.setVendorId(menuItem.getVendorId());
        dto.setName(menuItem.getName());
        dto.setDescription(menuItem.getDescription());
        dto.setPrice(menuItem.getPrice());
        dto.setQuantity(menuItem.getQuantity());
        dto.setCategory(menuItem.getCategory());
        dto.setVeg(menuItem.isVeg());
        dto.setImageUrl(menuItem.getImageUrl());
        dto.setIsAvailable(menuItem.getIsAvailable());
        dto.setCreatedAt(menuItem.getCreatedAt());
        dto.setUpdatedAt(menuItem.getUpdatedAt());
        return dto;
    }

    public CategoriesResponseDto updateVendorCategories(String vendorId, List<String> categories) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
        vendor.setFoodCategories(categories);
        vendorRepository.save(vendor);
        return CategoriesResponseDto.builder()
                .categories(categories)
                .count(categories.size())
                .build();
    }

    public MenuItemResponseDto createMenuItem(String vendorId, MenuItemCreateDto createDto) {
        Vendor vendor = validateVendor(vendorId);

        if (menuItemRepository.existsByVendorIdAndName(vendorId, createDto.getName())) {
            throw new BadRequestException("Menu item with name '" + createDto.getName() + "' already exists in your store");
        }

        MenuItem menuItem = new MenuItem();
        menuItem.setVendorId(vendorId);
        menuItem.setName(createDto.getName().trim());
        menuItem.setDescription(createDto.getDescription() != null ? createDto.getDescription().trim() : null);
        menuItem.setPrice(createDto.getPrice());
        menuItem.setQuantity(createDto.getQuantity());
        menuItem.setCategory(createDto.getCategory().trim());
        menuItem.setVeg(createDto.isVeg());
        menuItem.setImageUrl(createDto.getImageUrl());
        menuItem.setIsAvailable(createDto.isAvailable());
        menuItem.setCreatedAt(LocalDateTime.now());
        menuItem.setUpdatedAt(LocalDateTime.now());

        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        log.info("Menu item created: {} for vendor: {}", savedMenuItem.getName(), vendorId);

        return mapToResponseDto(savedMenuItem);
    }

    public MenuItemResponseDto getMenuItemById(String menuItemId) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

        return mapToResponseDto(menuItem);
    }

    public Page<MenuItemResponseDto> getMenuItemsByVendor(String vendorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
        Page<MenuItem> menuItemPage = menuItemRepository.findByVendorId(vendorId, pageable);
        return menuItemPage.map(this::mapToResponseDto);
    }

    public MenuItemsResponseDto getAvailableMenuItemsByVendor(String vendorId) {
        List<MenuItem> menuItems = menuItemRepository.findByVendorIdAndIsAvailable(vendorId, true);
        List<MenuItemResponseDto> menuItemDtos = menuItems.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
        return MenuItemsResponseDto.builder()
                .menuItems(menuItemDtos)
                .count(menuItemDtos.size())
                .build();
    }

    public MenuItemsResponseDto getMenuItemsByCategory(String vendorId, String category) {
        List<MenuItem> menuItems = menuItemRepository.findByVendorIdAndCategory(vendorId, category);
        List<MenuItemResponseDto> menuItemDtos = menuItems.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
        return MenuItemsResponseDto.builder()
                .menuItems(menuItemDtos)
                .count(menuItemDtos.size())
                .build();
    }

    public MenuItemsResponseDto searchMenuItems(String vendorId, String searchTerm) {
        List<MenuItem> menuItems = menuItemRepository.findByVendorIdAndNameContainingIgnoreCase(vendorId, searchTerm);
        List<MenuItemResponseDto> menuItemDtos = menuItems.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
        return MenuItemsResponseDto.builder()
                .menuItems(menuItemDtos)
                .count(menuItemDtos.size())
                .build();
    }

    public MenuItemsResponseDto getMenuItemsByPriceRange(String vendorId, double minPrice, double maxPrice) {
        List<MenuItem> menuItems = menuItemRepository.findByVendorIdAndPriceBetween(vendorId, minPrice, maxPrice);
        List<MenuItemResponseDto> menuItemDtos = menuItems.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
        return MenuItemsResponseDto.builder()
                .menuItems(menuItemDtos)
                .count(menuItemDtos.size())
                .build();
    }


    public CategoriesResponseDto getMenuCategories(String vendorId) {
        List<MenuItem> menuItems = menuItemRepository.findByVendorId(vendorId);
        List<String> categories = menuItems.stream()
                .map(MenuItem::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        return CategoriesResponseDto.builder()
                .categories(categories)
                .count(categories.size())
                .build();
    }

    public MenuItemResponseDto updateMenuItem(String vendorId, String menuItemId, UpdateMenuItemDto updateDto) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

        if (updateDto.getName() != null && !updateDto.getName().equals(menuItem.getName())) {
            if (menuItemRepository.existsByVendorIdAndName(vendorId, updateDto.getName())) {
                throw new BadRequestException("Menu item with name '" + updateDto.getName() + "' already exists in your store");
            }
            menuItem.setName(updateDto.getName().trim());
        }

        if (updateDto.getDescription() != null) {
            menuItem.setDescription(updateDto.getDescription().trim());
        }
        if (updateDto.getPrice() != null) {
            menuItem.setPrice(updateDto.getPrice());
        }
        if (updateDto.getQuantity() != null) {
            menuItem.setQuantity(updateDto.getQuantity());
        }
        if (updateDto.getCategory() != null) {
            menuItem.setCategory(updateDto.getCategory().trim());
        }
        if (updateDto.getVeg() != null) {
            menuItem.setVeg(updateDto.getVeg());
        }
        if (updateDto.getImageUrl() != null) {
            menuItem.setImageUrl(updateDto.getImageUrl());
        }
        if (updateDto.getIsAvailable() != null) {
            menuItem.setIsAvailable(updateDto.getIsAvailable());
        }
        menuItem.setUpdatedAt(LocalDateTime.now());

        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
        log.info("Menu item updated: {} for vendor: {}", updatedMenuItem.getName(), vendorId);

        return mapToResponseDto(updatedMenuItem);
    }

    public MenuItemResponseDto toggleAvailability(String vendorId, String menuItemId) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

        menuItem.setIsAvailable(!menuItem.getIsAvailable());
        menuItem.setUpdatedAt(LocalDateTime.now());

        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
        log.info("Menu item availability toggled: {} to {} for vendor: {}",
                updatedMenuItem.getName(), updatedMenuItem.getIsAvailable(), vendorId);

        return mapToResponseDto(updatedMenuItem);
    }

    public MenuItemResponseDto updateQuantity(String vendorId, String menuItemId, int newQuantity) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

        if (newQuantity < 0) {
            throw new BadRequestException("Quantity cannot be negative");
        }
        menuItem.setQuantity(newQuantity);
        if (newQuantity == 0) {
            menuItem.setIsAvailable(false);
        }
        menuItem.setUpdatedAt(LocalDateTime.now());

        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
        log.info("Menu item quantity updated: {} to {} for vendor: {}",
                updatedMenuItem.getName(), newQuantity, vendorId);

        return mapToResponseDto(updatedMenuItem);
    }

    public void deleteMenuItem(String vendorId, String menuItemId) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

        menuItemRepository.delete(menuItem);
        log.info("Menu item deleted: {} for vendor: {}", menuItem.getName(), vendorId);
    }

    public void deleteMultipleMenuItems(String vendorId, List<String> menuItemIds) {
        List<MenuItem> menuItems = menuItemRepository.findAllById(menuItemIds);

        menuItemRepository.deleteAll(menuItems);
        log.info("Bulk delete: {} items for vendor: {}", menuItems.size(), vendorId);
    }

    public Map<String, Long> getMenuItemStats(String vendorId) {
        List<MenuItem> allItems = menuItemRepository.findByVendorId(vendorId);
        long availableItems = allItems.stream().filter(MenuItem::getIsAvailable).count();

        return Map.of(
                "totalItems", (long) allItems.size(),
                "availableItems", availableItems,
                "unavailableItems", allItems.size() - availableItems
        );
    }

    public MenuItemsResponseDto getPublicMenuByVendor(String vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

        if (vendor.getVerificationStatus() != VendorVerificationStatus.VERIFIED || vendor.isSuspended()) {
            throw new BadRequestException("Vendor is not available");
        }

        List<MenuItem> menuItems = menuItemRepository.findByVendorIdAndIsAvailable(vendorId, true);
        List<MenuItemResponseDto> menuItemDtos = menuItems.stream()
                .filter(item -> item.getQuantity() > 0)
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
        return MenuItemsResponseDto.builder()
                .menuItems(menuItemDtos)
                .count(menuItemDtos.size())
                .build();
    }

    public MenuItemsResponseDto getPublicMenuByCategory(String vendorId, String category) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

        if (vendor.getVerificationStatus() != VendorVerificationStatus.VERIFIED || vendor.isSuspended()) {
            throw new BadRequestException("Vendor is not available");
        }

        List<MenuItem> menuItems = menuItemRepository.findByVendorIdAndCategory(vendorId, category);
        List<MenuItemResponseDto> menuItemDtos = menuItems.stream()
                .filter(item -> item.getIsAvailable() && item.getQuantity() > 0)
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
        return MenuItemsResponseDto.builder()
                .menuItems(menuItemDtos)
                .count(menuItemDtos.size())
                .build();
    }

}