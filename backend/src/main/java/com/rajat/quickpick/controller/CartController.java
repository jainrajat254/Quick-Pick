package com.rajat.quickpick.controller;

import com.rajat.quickpick.dto.cart.AddToCartDto;
import com.rajat.quickpick.dto.cart.CartResponseDto;
import com.rajat.quickpick.dto.cart.UpdateCartItemDto;
import com.rajat.quickpick.security.JwtUtil;
import com.rajat.quickpick.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final JwtUtil jwtUtil;

    @PostMapping("/items")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<CartResponseDto> addToCart(@Valid @RequestBody AddToCartDto addToCartDto,
                                                     HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        CartResponseDto cart = cartService.addToCart(userId, addToCartDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }

    @GetMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<CartResponseDto> getCart(HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        CartResponseDto cart = cartService.getCart(userId);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/items/{menuItemId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<CartResponseDto> updateCartItem(@PathVariable String menuItemId,
                                                          @Valid @RequestBody UpdateCartItemDto updateDto,
                                                          HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        CartResponseDto cart = cartService.updateCartItem(userId, menuItemId, updateDto);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/items/{menuItemId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<CartResponseDto> removeFromCart(@PathVariable String menuItemId,
                                                          HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        CartResponseDto cart = cartService.removeFromCart(userId, menuItemId);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Map<String, String>> clearCart(HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        cartService.clearCart(userId);
        return ResponseEntity.ok(Map.of("message", "Cart cleared successfully"));
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

