package com.rajat.quickpick.service;

import com.rajat.quickpick.dto.order.*;
import com.rajat.quickpick.enums.OrderStatus;
import com.rajat.quickpick.enums.VendorVerificationStatus;
import com.rajat.quickpick.exception.BadRequestException;
import com.rajat.quickpick.exception.ResourceNotFoundException;
import com.rajat.quickpick.model.*;
import com.rajat.quickpick.repository.*;
import com.rajat.quickpick.service.OrderNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final VendorRepository vendorRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderNotificationService notificationService;
    private final CartRepository cartRepository;

    public OrderResponseDto createOrder(String userId, CreateOrderDto createDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.isSuspended()) {
            throw new BadRequestException("Your account is suspended");
        }

        Vendor vendor = vendorRepository.findById(createDto.getVendorId())
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

        if (vendor.isSuspended() || vendor.getVerificationStatus() != VendorVerificationStatus.VERIFIED) {
            throw new BadRequestException("Vendor is not available");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;

        for (OrderItemDto itemDto : createDto.getOrderItems()) {
            MenuItem menuItem = menuItemRepository.findById(itemDto.getMenuItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Menu item not found: " + itemDto.getMenuItemId()));

            if (!menuItem.getVendorId().equals(createDto.getVendorId())) {
                throw new BadRequestException("Menu item does not belong to this vendor");
            }

            if (!menuItem.getIsAvailable()) {
                throw new BadRequestException("Menu item '" + menuItem.getName() + "' is not available");
            }

            if (menuItem.getQuantity() < itemDto.getQuantity()) {
                throw new BadRequestException("Insufficient quantity for '" + menuItem.getName() + "'. Available: " + menuItem.getQuantity());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setMenuItemId(menuItem.getId());
            orderItem.setMenuItemName(menuItem.getName());
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setUnitPrice(menuItem.getPrice());
            orderItem.setTotalPrice(menuItem.getPrice() * itemDto.getQuantity());

            orderItems.add(orderItem);
            totalAmount += orderItem.getTotalPrice();

            menuItem.setQuantity(menuItem.getQuantity() - itemDto.getQuantity());
            if (menuItem.getQuantity() == 0) {
                menuItem.setIsAvailable(false);
            }
            menuItem.setUpdatedAt(LocalDateTime.now());
            menuItemRepository.save(menuItem);
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setVendorId(createDto.getVendorId());
        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setSpecialInstructions(createDto.getSpecialInstructions());
        order.setDeliveredToVendor(false);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);
        log.info("Order created: {} for user: {} with vendor: {}", savedOrder.getId(), userId, createDto.getVendorId());


        notificationService.notifyNewOrder(savedOrder, vendor);

        return mapToResponseDto(savedOrder, user, vendor);
    }

    public OrderResponseDto updateOrderStatus(String vendorId, String orderId, UpdateOrderStatusDto updateDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getVendorId().equals(vendorId)) {
            throw new BadRequestException("You can only update your own orders");
        }

        validateStatusTransition(order.getOrderStatus(), updateDto.getOrderStatus());

        OrderStatus oldStatus = order.getOrderStatus();
        order.setOrderStatus(updateDto.getOrderStatus());
        order.setUpdatedAt(LocalDateTime.now());

        Order updatedOrder = orderRepository.save(order);
        log.info("Order status updated: {} from {} to {}", orderId, oldStatus, updateDto.getOrderStatus());

        User user = userRepository.findById(order.getUserId()).orElse(null);
        Vendor vendor = vendorRepository.findById(vendorId).orElse(null);

        notificationService.notifyOrderStatusUpdate(updatedOrder, user);

        return mapToResponseDto(updatedOrder, user, vendor);
    }

    public List<OrderResponseDto> getPendingOrdersForVendor(String vendorId) {
        List<Order> undeliveredOrders = orderRepository.findByVendorIdAndDeliveredToVendorOrderByCreatedAtAsc(
                vendorId, false);

        undeliveredOrders.forEach(order -> {
            order.setDeliveredToVendor(true);
            order.setDeliveredToVendorAt(LocalDateTime.now());
        });

        if (!undeliveredOrders.isEmpty()) {
            orderRepository.saveAll(undeliveredOrders);
            log.info("Delivered {} pending orders to vendor: {}", undeliveredOrders.size(), vendorId);
        }

        List<OrderResponseDto> orderDtos = undeliveredOrders.stream()
                .map(order -> {
                    User user = userRepository.findById(order.getUserId()).orElse(null);
                    Vendor vendor = vendorRepository.findById(vendorId).orElse(null);
                    return mapToResponseDto(order, user, vendor);
                })
                .collect(Collectors.toList());

        return OrdersResponseDto.builder()
                .orders(orderDtos)
                .count(orderDtos.size())
                .build().getOrders();
    }

    public OrderResponseDto getOrderById(String orderId, String userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getUserId().equals(userId)) {
            throw new BadRequestException("Access denied");
        }

        User user = userRepository.findById(order.getUserId()).orElse(null);
        Vendor vendor = vendorRepository.findById(order.getVendorId()).orElse(null);

        return mapToResponseDto(order, user, vendor);
    }

    public OrderResponseDto getVendorOrderById(String vendorId, String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getVendorId().equals(vendorId)) {
            throw new BadRequestException("Access denied");
        }

        User user = userRepository.findById(order.getUserId()).orElse(null);
        Vendor vendor = vendorRepository.findById(vendorId).orElse(null);

        return mapToResponseDto(order, user, vendor);
    }

    public OrdersResponseDto getUserOrders(String userId) {
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        List<OrderResponseDto> orderDtos = orders.stream()
                .map(order -> {
                    User user = userRepository.findById(userId).orElse(null);
                    Vendor vendor = vendorRepository.findById(order.getVendorId()).orElse(null);
                    return mapToResponseDto(order, user, vendor);
                })
                .collect(Collectors.toList());

        return OrdersResponseDto.builder()
                .orders(orderDtos)
                .count(orderDtos.size())
                .build();
    }



    public Page<OrderResponseDto> getVendorOrders(String vendorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Order> orderPage = orderRepository.findByVendorId(vendorId, pageable);

        return orderPage.map(order -> {
            User user = userRepository.findById(order.getUserId()).orElse(null);
            Vendor vendor = vendorRepository.findById(vendorId).orElse(null);
            return mapToResponseDto(order, user, vendor);
        });
    }

    public Page<OrderResponseDto> getVendorOrdersPaginated(String vendorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Order> orderPage = orderRepository.findByVendorId(vendorId, pageable);

        return orderPage.map(order -> {
            User user = userRepository.findById(order.getUserId()).orElse(null);
            Vendor vendor = vendorRepository.findById(vendorId).orElse(null);
            return mapToResponseDto(order, user, vendor);
        });
    }

    public OrdersResponseDto getOrdersByStatus(String userId, OrderStatus status) {
        List<Order> orders = orderRepository.findByUserIdAndOrderStatus(userId, status);
        List<OrderResponseDto> orderDtos = orders.stream()
                .map(order -> {
                    User user = userRepository.findById(userId).orElse(null);
                    Vendor vendor = vendorRepository.findById(order.getVendorId()).orElse(null);
                    return mapToResponseDto(order, user, vendor);
                })
                .collect(Collectors.toList());

        return OrdersResponseDto.builder()
                .orders(orderDtos)
                .count(orderDtos.size())
                .build();
    }

    public OrdersResponseDto getVendorOrdersByStatus(String vendorId, OrderStatus status) {
        List<Order> orders = orderRepository.findByVendorIdAndOrderStatus(vendorId, status);
        List<OrderResponseDto> orderDtos = orders.stream()
                .map(order -> {
                    User user = userRepository.findById(order.getUserId()).orElse(null);
                    Vendor vendor = vendorRepository.findById(vendorId).orElse(null);
                    return mapToResponseDto(order, user, vendor);
                })
                .collect(Collectors.toList());

        return OrdersResponseDto.builder()
                .orders(orderDtos)
                .count(orderDtos.size())
                .build();
    }

    public void cancelOrder(String userId, String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getUserId().equals(userId)) {
            throw new BadRequestException("You can only cancel your own orders");
        }

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new BadRequestException("Order cannot be cancelled. Current status: " + order.getOrderStatus());
        }

        restoreMenuItemQuantities(order.getOrderItems());

        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        log.info("Order cancelled: {} by user: {}", orderId, userId);

        Vendor vendor = vendorRepository.findById(order.getVendorId()).orElse(null);
        if (vendor != null) {
            notificationService.notifyOrderCancellation(order, vendor);
        }
    }

    public OrderStatsDto getUserOrderStats(String userId) {
        OrderStatsDto stats = new OrderStatsDto();
        stats.setTotalOrders(orderRepository.countByUserId(userId));
        stats.setPendingOrders(orderRepository.countByUserIdAndOrderStatus(userId, OrderStatus.PENDING));
        stats.setCompletedOrders(orderRepository.countByUserIdAndOrderStatus(userId, OrderStatus.COMPLETED));
        stats.setCancelledOrders(orderRepository.countByUserIdAndOrderStatus(userId, OrderStatus.CANCELLED));

        // Calculate total spent
        List<Order> completedOrders = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.COMPLETED);
        double totalSpent = completedOrders.stream().mapToDouble(Order::getTotalAmount).sum();

        return stats;
    }

    public OrderStatsDto getVendorOrderStats(String vendorId) {
        OrderStatsDto stats = new OrderStatsDto();
        stats.setTotalOrders(orderRepository.countByVendorId(vendorId));
        stats.setPendingOrders(orderRepository.countByVendorIdAndOrderStatus(vendorId, OrderStatus.PENDING));
        stats.setCompletedOrders(orderRepository.countByVendorIdAndOrderStatus(vendorId, OrderStatus.COMPLETED));
        stats.setCancelledOrders(orderRepository.countByVendorIdAndOrderStatus(vendorId, OrderStatus.CANCELLED));

        List<Order> completedOrders = orderRepository.findByVendorIdAndOrderStatus(vendorId, OrderStatus.COMPLETED);

        return stats;
    }

    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        switch (currentStatus) {
            case PENDING:
                if (newStatus != OrderStatus.ACCEPTED && newStatus != OrderStatus.REJECTED) {
                    throw new BadRequestException("Invalid status transition from PENDING to " + newStatus);
                }
                break;
            case ACCEPTED:
                if (newStatus != OrderStatus.PREPARING) {
                    throw new BadRequestException("Invalid status transition from ACCEPTED to " + newStatus);
                }
                break;
            case PREPARING:
                if (newStatus != OrderStatus.READY_FOR_PICKUP) {
                    throw new BadRequestException("Invalid status transition from PREPARING to " + newStatus);
                }
                break;
            case READY_FOR_PICKUP:
                if (newStatus != OrderStatus.COMPLETED) {
                    throw new BadRequestException("Invalid status transition from READY_FOR_PICKUP to " + newStatus);
                }
                break;
            case COMPLETED:
            case CANCELLED:
            case REJECTED:
                throw new BadRequestException("Cannot change status from " + currentStatus);
            default:
                throw new BadRequestException("Unknown order status: " + currentStatus);
        }
    }

    private void restoreMenuItemQuantities(List<OrderItem> orderItems) {
        for (OrderItem item : orderItems) {
            MenuItem menuItem = menuItemRepository.findById(item.getMenuItemId()).orElse(null);
            if (menuItem != null) {
                menuItem.setQuantity(menuItem.getQuantity() + item.getQuantity());
                menuItem.setIsAvailable(true);
                menuItem.setUpdatedAt(LocalDateTime.now());
                menuItemRepository.save(menuItem);
            }
        }
    }

    private OrderResponseDto mapToResponseDto(Order order, User user, Vendor vendor) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setVendorId(order.getVendorId());
        dto.setVendorName(vendor != null ? vendor.getVendorName() : "Unknown Vendor");
        dto.setStoreName(vendor != null ? vendor.getStoreName() : "Unknown Store");
        dto.setOrderItems(order.getOrderItems());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setSpecialInstructions(order.getSpecialInstructions());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        return dto;
    }

    public OrderResponseDto createOrderFromCart(String userId) {
        // Get user's cart
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException("Cart is empty"));

        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        // Create order DTO from cart
        CreateOrderDto createDto = new CreateOrderDto();
        createDto.setVendorId(cart.getVendorId());

        List<OrderItemDto> orderItemDtos = cart.getItems().stream()
                .map(cartItem -> {
                    OrderItemDto dto = new OrderItemDto();
                    dto.setMenuItemId(cartItem.getMenuItemId());
                    dto.setQuantity(cartItem.getQuantity());
                    return dto;
                })
                .collect(Collectors.toList());

        createDto.setOrderItems(orderItemDtos);

        // Create the order using existing logic
        OrderResponseDto orderResponse = createOrder(userId, createDto);

        // Clear the cart after successful order
        cartRepository.deleteByUserId(userId);
        log.info("Cart cleared after order creation for user: {}", userId);

        return orderResponse;
    }
}
