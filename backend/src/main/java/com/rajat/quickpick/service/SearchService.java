package com.rajat.quickpick.service;

import com.rajat.quickpick.dto.menu.MenuItemSearchDto;
import com.rajat.quickpick.dto.vendor.*;
import com.rajat.quickpick.dto.menu.MenuItemResponseDto;
import com.rajat.quickpick.enums.VendorVerificationStatus;
import com.rajat.quickpick.model.*;
import com.rajat.quickpick.repository.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private UserRepository userRepository;

    private String getUserCollege(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user != null ? user.getCollegeName() : null;
    }


    public List<VendorResponseDto> searchVendors(String userId, String searchQuery) {
        String userCollege = getUserCollege(userId);
        if (userCollege == null) {
            return List.of();
        }

        List<Vendor> vendors;

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            vendors = vendorRepository.findByCollegeNameAndSearchQuery(userCollege, searchQuery);
        } else {
            vendors = vendorRepository.findByCollegeNameAndVerificationStatus(
                    userCollege, VendorVerificationStatus.VERIFIED);
        }

        vendors =  vendors.stream()
                .filter(vendor -> !vendor.isSuspended())
                .collect(Collectors.toList());

        return vendors.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public Page<MenuItemResponseDto> searchMenuItems(String userId, MenuItemSearchDto criteria, Pageable pageable) {
        String userCollege = getUserCollege(userId);
        if (userCollege == null) {
            return Page.empty(pageable);
        }
        Page<MenuItem> menuItemPage = performMenuItemSearch(userCollege, criteria, pageable);

        List<MenuItemResponseDto> menuItemDtos = menuItemPage.getContent().stream()
                .map(this::mapToMenuItemDto)
                .collect(Collectors.toList());

        menuItemDtos = applyMenuItemSorting(menuItemDtos, criteria.getSortBy(), criteria.getSortDirection());

        return new PageImpl<>(menuItemDtos, pageable, menuItemPage.getTotalElements());
    }

    private Page<MenuItem> performMenuItemSearch(String collegeName, MenuItemSearchDto criteria, Pageable pageable) {
        List<Vendor> collegeVendors = vendorRepository.findByCollegeNameAndVerificationStatus(
                collegeName, VendorVerificationStatus.VERIFIED);
        List<String> vendorIds = collegeVendors.stream().map(Vendor::getId).collect(Collectors.toList());

        if (criteria.getVendorId() != null) {
            if (!vendorIds.contains(criteria.getVendorId())) {
                return Page.empty(pageable);
            }

            if (criteria.getSearchQuery() != null && !criteria.getSearchQuery().trim().isEmpty()) {
                return menuItemRepository.findByVendorIdAndNameContainingIgnoreCase(
                        criteria.getVendorId(), criteria.getSearchQuery(), pageable);
            } else {
                return menuItemRepository.findByVendorId(criteria.getVendorId(), pageable);
            }
        } else {
            if (criteria.getSearchQuery() != null && !criteria.getSearchQuery().trim().isEmpty()) {
                return menuItemRepository.findByVendorIdsAndNameContainingIgnoreCaseAndAvailable(
                        vendorIds, criteria.getSearchQuery(), pageable);
            } else {
                return menuItemRepository.findByVendorIdIn(vendorIds, pageable);
            }
        }
    }

    public List<MenuItem> applyMenuItemFilters(List<MenuItem> menuItems, MenuItemSearchDto criteria) {
        return menuItems.stream()
                .filter(item -> !criteria.isAvailableOnly() || item.getIsAvailable())
                .filter(item -> criteria.getCategory() == null ||
                        item.getCategory().equalsIgnoreCase(criteria.getCategory()))
                .filter(item -> criteria.getMinPrice() == null ||
                        item.getPrice() >= criteria.getMinPrice())
                .filter(item -> criteria.getMaxPrice() == null ||
                        item.getPrice() <= criteria.getMaxPrice())
                //will add later.....
                // .filter(item -> !criteria.isHasActiveOffer() ||
                //         item.getActiveOffer() != null && item.getActiveOffer().isActive())
                .collect(Collectors.toList());
    }


    public VendorResponseDto getVendorById(String id) {
        Vendor vendor = vendorRepository.findById(id).orElse(null);
        return vendor != null ? mapToDto(vendor) : null;
    }

    private List<MenuItemResponseDto> applyMenuItemSorting(List<MenuItemResponseDto> menuItems,
                                                           String sortBy, String sortDirection) {
        boolean ascending = "ASC".equalsIgnoreCase(sortDirection);

        if (sortBy == null) return menuItems;

        switch (sortBy.toLowerCase()) {
            case "name":
                return menuItems.stream()
                        .sorted((m1, m2) -> ascending ?
                                m1.getName().compareTo(m2.getName()) :
                                m2.getName().compareTo(m1.getName()))
                        .collect(Collectors.toList());
            case "price":
                return menuItems.stream()
                        .sorted((m1, m2) -> ascending ?
                                Double.compare(m1.getPrice(), m2.getPrice()) :
                                Double.compare(m2.getPrice(), m1.getPrice()))
                        .collect(Collectors.toList());
            default:
                return menuItems;
        }
    }


    public Page<MenuItemResponseDto> strictSearchMenuItems(String userId, MenuItemSearchDto criteria, Pageable pageable) {
        String userCollege = getUserCollege(userId);
        if (userCollege == null) {
            return Page.empty(pageable);
        }

        List<Vendor> collegeVendors = vendorRepository.findByCollegeNameAndVerificationStatus(
                userCollege, VendorVerificationStatus.VERIFIED);
        List<String> vendorIds = collegeVendors.stream().map(Vendor::getId).collect(Collectors.toList());
        List<MenuItem> menuItems = menuItemRepository.findByVendorIdIn(vendorIds, pageable).getContent();

        List<MenuItemResponseDto> filtered = menuItems.stream()
                .filter(item -> criteria.getSearchQuery() == null ||
                        item.getName().toLowerCase().contains(criteria.getSearchQuery().toLowerCase()))
                .filter(item -> criteria.getCategory() == null ||
                        item.getCategory().equalsIgnoreCase(criteria.getCategory()))
                .filter(item -> criteria.getVendorId() == null ||
                        item.getVendorId().equals(criteria.getVendorId()))
                .filter(item -> !criteria.isAvailableOnly() || item.getIsAvailable())
                .filter(item -> criteria.getMinPrice() == null ||
                        item.getPrice() >= criteria.getMinPrice())
                .filter(item -> criteria.getMaxPrice() == null ||
                        item.getPrice() <= criteria.getMaxPrice())
                .map(this::mapToMenuItemDto)
                .collect(Collectors.toList());

        int start = pageable.getPageNumber() * pageable.getPageSize();
        int end = Math.min(start + pageable.getPageSize(), filtered.size());
        List<MenuItemResponseDto> pageContent = start < filtered.size() ? filtered.subList(start, end) : List.of();

        return new PageImpl<>(pageContent, pageable, filtered.size());
    }



    private VendorResponseDto mapToDto(Vendor vendor) {
        VendorResponseDto dto = new VendorResponseDto();
        dto.setId(vendor.getId());
        dto.setVendorName(vendor.getVendorName());
        dto.setStoreName(vendor.getStoreName());
        dto.setEmail(vendor.getEmail());
        dto.setPhone(vendor.getPhone());
        dto.setAddress(vendor.getAddress());
        dto.setCollegeName(vendor.getCollegeName());
        dto.setVendorDescription(vendor.getVendorDescription());
        dto.setFoodCategories(vendor.getFoodCategories());
        dto.setProfileImageUrl(vendor.getProfileImageUrl());
        dto.setRole(vendor.getRole());
        dto.setPhoneVerified(vendor.isPhoneVerified());
        dto.setEmailVerified(vendor.isEmailVerified());
        return dto;
    }

    public MenuItemResponseDto mapToMenuItemDto(MenuItem menuItem) {
        MenuItemResponseDto dto = new MenuItemResponseDto();
        dto.setId(menuItem.getId());
        dto.setVendorId(menuItem.getVendorId());
        dto.setName(menuItem.getName());
        dto.setDescription(menuItem.getDescription());
        dto.setPrice(menuItem.getPrice());
        dto.setQuantity(menuItem.getQuantity());
        dto.setCategory(menuItem.getCategory());
        dto.setImageUrl(menuItem.getImageUrl());
        dto.setIsAvailable(menuItem.getIsAvailable());
        dto.setCreatedAt(menuItem.getCreatedAt());
        dto.setUpdatedAt(menuItem.getUpdatedAt());
        return dto;
    }
}