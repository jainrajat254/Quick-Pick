package com.rajat.quickpick.controller;


import com.rajat.quickpick.dto.order.*;
import com.rajat.quickpick.enums.OrderStatus;
import com.rajat.quickpick.security.JwtUtil;
import com.rajat.quickpick.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody CreateOrderDto createDto,
                                                        HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        OrderResponseDto order = orderService.createOrder(userId, createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PostMapping("/from-cart")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<OrderResponseDto> createOrderFromCart(HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        OrderResponseDto order = orderService.createOrderFromCart(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable String orderId,
                                                         HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        OrderResponseDto order = orderService.getOrderById(orderId, userId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/my-orders")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<OrdersResponseDto> getMyOrders(HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        OrdersResponseDto response = orderService.getUserOrders(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vendor/orders/pending")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<OrdersResponseDto> getPendingOrders(HttpServletRequest request,
                                                              @RequestParam(required = false) String otp) {
        String vendorId = extractUserIdFromToken(request);
        OrdersResponseDto response = (OrdersResponseDto) orderService.getPendingOrdersForVendor(vendorId, otp);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/my-orders/status/{status}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<OrdersResponseDto> getMyOrdersByStatus(@PathVariable OrderStatus status,
                                                                 HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        OrdersResponseDto response = orderService.getOrdersByStatus(userId, status);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{orderId}/cancel")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Map<String, String>> cancelOrder(@PathVariable String orderId,
                                                           HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        orderService.cancelOrder(userId, orderId);
        return ResponseEntity.ok(Map.of("message", "Order cancelled successfully"));
    }

    @GetMapping("/my-orders/stats")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<OrderStatsDto> getMyOrderStats(HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        OrderStatsDto stats = orderService.getUserOrderStats(userId);
        return ResponseEntity.ok(stats);
    }



    @GetMapping("/vendor/orders/paginated")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<Page<OrderResponseDto>> getVendorOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest request) {
        String vendorId = extractUserIdFromToken(request);
        Page<OrderResponseDto> orders = orderService.getVendorOrdersPaginated(vendorId, page, size);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/vendor/orders/{orderId}")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<OrderResponseDto> getVendorOrderById(@PathVariable String orderId,
                                                               HttpServletRequest request) {
        String vendorId = extractUserIdFromToken(request);
        OrderResponseDto order = orderService.getVendorOrderById(vendorId, orderId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/vendor/orders/status/{status}")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<OrdersResponseDto> getVendorOrdersByStatus(@PathVariable OrderStatus status,
                                                                     HttpServletRequest request) {
        String vendorId = extractUserIdFromToken(request);
        OrdersResponseDto response = orderService.getVendorOrdersByStatus(vendorId, status);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/vendor/orders/{orderId}/status")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable String orderId,
                                                              @Valid @RequestBody UpdateOrderStatusDto updateDto,
                                                              HttpServletRequest request) {
        String vendorId = extractUserIdFromToken(request);
        OrderResponseDto order = orderService.updateOrderStatus(vendorId, orderId, updateDto);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/vendor/orders/stats")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<OrderStatsDto> getVendorOrderStats(HttpServletRequest request) {
        String vendorId = extractUserIdFromToken(request);
        OrderStatsDto stats = orderService.getVendorOrderStats(vendorId);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/my-orders/grouped")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Map<String, OrdersResponseDto>> getMyOrdersGrouped(HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        Map<String, OrdersResponseDto> response = orderService.getUserOrdersGrouped(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vendor/orders/grouped")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<Map<String, OrdersResponseDto>> getVendorOrdersGrouped(HttpServletRequest request) {
        String vendorId = extractUserIdFromToken(request);
        Map<String, OrdersResponseDto> response = orderService.getVendorOrdersGrouped(vendorId);
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