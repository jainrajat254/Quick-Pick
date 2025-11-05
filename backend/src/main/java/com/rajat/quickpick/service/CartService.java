package com.rajat.quickpick.service;

import com.rajat.quickpick.dto.cart.AddToCartDto;
import com.rajat.quickpick.dto.cart.CartResponseDto;
import com.rajat.quickpick.dto.cart.UpdateCartItemDto;
import com.rajat.quickpick.exception.BadRequestException;
import com.rajat.quickpick.exception.ResourceNotFoundException;
import com.rajat.quickpick.model.*;
import com.rajat.quickpick.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final MenuItemRepository menuItemRepository;
    private final VendorRepository vendorRepository;
    private final UserRepository userRepository;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public CartResponseDto addToCart(String userId, AddToCartDto addToCartDto) {
        // Validate user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.isSuspended()) {
            throw new BadRequestException("Your account is suspended");
        }

        // Get menu item
        MenuItem menuItem = menuItemRepository.findById(addToCartDto.getMenuItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

        // Validate menu item availability
        if (!menuItem.getIsAvailable()) {
            throw new BadRequestException("Menu item '" + menuItem.getName() + "' is not available");
        }

        if (menuItem.getQuantity() < addToCartDto.getQuantity()) {
            throw new BadRequestException("Insufficient quantity for '" + menuItem.getName() + "'. Available: " + menuItem.getQuantity());
        }

        // Get vendor details
        Vendor vendor = vendorRepository.findById(menuItem.getVendorId())
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

        // Get or create cart
        Cart cart = cartRepository.findByUserId(userId).orElse(null);

        if (cart == null) {
            // Create new cart
            cart = new Cart();
            cart.setUserId(userId);
            cart.setVendorId(menuItem.getVendorId());
            cart.setVendorName(vendor.getStoreName());
            cart.setCreatedAt(LocalDateTime.now());
        } else {
            // Validate all items are from same vendor
            if (!cart.getVendorId().equals(menuItem.getVendorId())) {
                throw new BadRequestException("Cannot add items from different vendors. Please clear your cart first or complete the current order.");
            }
        }

        // Check if item already exists in cart
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getMenuItemId().equals(menuItem.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            // Update quantity
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + addToCartDto.getQuantity();

            if (menuItem.getQuantity() < newQuantity) {
                throw new BadRequestException("Cannot add more. Maximum available quantity: " + menuItem.getQuantity());
            }

            item.setQuantity(newQuantity);
            item.setTotalPrice(item.getUnitPrice() * newQuantity);
        } else {
            // Add new item
            CartItem cartItem = new CartItem();
            cartItem.setMenuItemId(menuItem.getId());
            cartItem.setMenuItemName(menuItem.getName());
            cartItem.setMenuItemImage(menuItem.getImageUrl());
            cartItem.setQuantity(addToCartDto.getQuantity());
            cartItem.setUnitPrice(menuItem.getPrice());
            cartItem.setTotalPrice(menuItem.getPrice() * addToCartDto.getQuantity());
            cartItem.setAvailable(menuItem.getIsAvailable());
            cartItem.setVeg(menuItem.isVeg());

            cart.getItems().add(cartItem);
        }

        // Recalculate total
        cart.setTotalAmount(cart.getItems().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum());

        cart.setUpdatedAt(LocalDateTime.now());

        Cart savedCart = cartRepository.save(cart);
        log.info("Cart updated for user: {} with item: {}", userId, menuItem.getName());

        return mapToCartResponseDto(savedCart);
    }

    public CartResponseDto getCart(String userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElse(createEmptyCart(userId));

        // Refresh item availability and prices
        refreshCartItems(cart);

        return mapToCartResponseDto(cart);
    }

    public CartResponseDto updateCartItem(String userId, String menuItemId, UpdateCartItemDto updateDto) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getMenuItemId().equals(menuItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item not found in cart"));

        // Validate quantity against available stock
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

        if (menuItem.getQuantity() < updateDto.getQuantity()) {
            throw new BadRequestException("Insufficient quantity. Available: " + menuItem.getQuantity());
        }

        cartItem.setQuantity(updateDto.getQuantity());
        cartItem.setTotalPrice(cartItem.getUnitPrice() * updateDto.getQuantity());

        // Recalculate total
        cart.setTotalAmount(cart.getItems().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum());

        cart.setUpdatedAt(LocalDateTime.now());

        Cart savedCart = cartRepository.save(cart);
        log.info("Cart item updated for user: {}", userId);

        return mapToCartResponseDto(savedCart);
    }

    public CartResponseDto removeFromCart(String userId, String menuItemId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        boolean removed = cart.getItems().removeIf(item -> item.getMenuItemId().equals(menuItemId));

        if (!removed) {
            throw new ResourceNotFoundException("Item not found in cart");
        }

        // Recalculate total
        cart.setTotalAmount(cart.getItems().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum());

        cart.setUpdatedAt(LocalDateTime.now());

        // If cart is empty, delete it
        if (cart.getItems().isEmpty()) {
            cartRepository.delete(cart);
            log.info("Cart deleted for user: {} (empty)", userId);
            return mapToCartResponseDto(createEmptyCart(userId));
        }

        Cart savedCart = cartRepository.save(cart);
        log.info("Item removed from cart for user: {}", userId);

        return mapToCartResponseDto(savedCart);
    }

    public void clearCart(String userId) {
        cartRepository.deleteByUserId(userId);
        log.info("Cart cleared for user: {}", userId);
    }

    private void refreshCartItems(Cart cart) {
        if (cart.getItems().isEmpty()) {
            return;
        }

        boolean updated = false;

        for (CartItem cartItem : cart.getItems()) {
            Optional<MenuItem> menuItemOpt = menuItemRepository.findById(cartItem.getMenuItemId());

            if (menuItemOpt.isPresent()) {
                MenuItem menuItem = menuItemOpt.get();

                // Update availability
                if (cartItem.isAvailable() != menuItem.getIsAvailable()) {
                    cartItem.setAvailable(menuItem.getIsAvailable());
                    updated = true;
                }

                // Update price if changed
                if (cartItem.getUnitPrice() != menuItem.getPrice()) {
                    cartItem.setUnitPrice(menuItem.getPrice());
                    cartItem.setTotalPrice(menuItem.getPrice() * cartItem.getQuantity());
                    updated = true;
                }
            } else {
                // Item deleted, mark as unavailable
                cartItem.setAvailable(false);
                updated = true;
            }
        }

        if (updated) {
            // Recalculate total
            cart.setTotalAmount(cart.getItems().stream()
                    .mapToDouble(CartItem::getTotalPrice)
                    .sum());
            cart.setUpdatedAt(LocalDateTime.now());
            cartRepository.save(cart);
        }
    }

    private Cart createEmptyCart(String userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setTotalAmount(0.0);
        cart.setCreatedAt(LocalDateTime.now());
        cart.setUpdatedAt(LocalDateTime.now());
        return cart;
    }

    private CartResponseDto mapToCartResponseDto(Cart cart) {
        int itemCount = cart.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        return CartResponseDto.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .vendorId(cart.getVendorId())
                .vendorName(cart.getVendorName())
                .items(cart.getItems())
                .totalAmount(cart.getTotalAmount())
                .itemCount(itemCount)
                .createdAt(cart.getCreatedAt() != null ? cart.getCreatedAt().format(formatter) : null)
                .updatedAt(cart.getUpdatedAt() != null ? cart.getUpdatedAt().format(formatter) : null)
                .build();
    }
}

