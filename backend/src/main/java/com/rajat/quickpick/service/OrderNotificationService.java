package com.rajat.quickpick.service;

import com.rajat.quickpick.enums.OrderStatus;
import com.rajat.quickpick.model.Order;
import com.rajat.quickpick.model.User;
import com.rajat.quickpick.model.Vendor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.rajat.quickpick.dto.order.OrderNotificationDto;

//to vendor for order
//to user for status
//to vendor is cancelled

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderNotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;
    private final WebSocketConnectionManager connectionManager;

    public void notifyNewOrder(Order order, Vendor vendor) {
        OrderNotificationDto notification = OrderNotificationDto.builder()
                .orderId(order.getId())
                .type("NEW_ORDER")
                .title("New Order Received!")
                .message("You have received a new order #" + order.getId().substring(0, 8))
                .orderStatus(order.getOrderStatus())
                .totalAmount(order.getTotalAmount())
                .timestamp(order.getCreatedAt())
                .build();

        boolean sentViaWebSocket = false;
        if (connectionManager.isUserConnected(vendor.getId())) {
            try {
                messagingTemplate.convertAndSendToUser(
                        vendor.getId(),
                        "/queue/orders",
                        notification
                );

                sentViaWebSocket = true;
                log.info("Sent new order notification via WebSocket to vendor: {}", vendor.getId());
            } catch (Exception e) {
                log.error("Failed to send WebSocket notification to vendor: {}", vendor.getId(), e);
            }
        }

        if (!sentViaWebSocket) {
            notificationService.sendOrderNotification(order, "ORDER_CREATED", vendor.getId(), "VENDOR");
            log.info("Vendor {} is offline, sent FCM notification", vendor.getId());
        }
    }

    public void notifyOrderStatusUpdate(Order order, User user) {
        String templateKey = getTemplateKeyForStatus(order.getOrderStatus());
        String message = getStatusMessage(order.getOrderStatus());

        OrderNotificationDto notification = OrderNotificationDto.builder()
                .orderId(order.getId())
                .type("STATUS_UPDATE")
                .title("Order Status Updated")
                .message(message)
                .orderStatus(order.getOrderStatus())
                .totalAmount(order.getTotalAmount())
                .timestamp(order.getUpdatedAt())
                .build();

        if (user != null) {
            // Try WebSocket first
            if (connectionManager.isUserConnected(user.getId())) {
                try {
                    messagingTemplate.convertAndSendToUser(
                            user.getId(),
                            "/queue/orders",
                            notification
                    );
                    log.info("Sent order status update via WebSocket to user: {}", user.getId());
                } catch (Exception e) {
                    log.error("Failed to send WebSocket notification to user: {}", user.getId(), e);
                }
            }

            // Always send FCM for important status updates
            notificationService.sendOrderNotification(order, templateKey, user.getId(), "USER");
        }
    }

    public void notifyOrderCancellation(Order order, Vendor vendor) {
        OrderNotificationDto notification = OrderNotificationDto.builder()
                .orderId(order.getId())
                .type("ORDER_CANCELLED")
                .title("Order Cancelled")
                .message("Order #" + order.getId().substring(0, 8) + " has been cancelled")
                .orderStatus(order.getOrderStatus())
                .totalAmount(order.getTotalAmount())
                .timestamp(order.getUpdatedAt())
                .build();

        // Try WebSocket
        if (connectionManager.isUserConnected(vendor.getId())) {
            try {
                messagingTemplate.convertAndSendToUser(
                        vendor.getId(),
                        "/queue/orders",
                        notification
                );
                log.info("Sent order cancellation via WebSocket to vendor: {}", vendor.getId());
            } catch (Exception e) {
                log.error("Failed to send WebSocket notification to vendor: {}", vendor.getId(), e);
            }
        }

        // Always send FCM notification
        notificationService.sendOrderNotification(order, "ORDER_CANCELLED", vendor.getId(), "VENDOR");
    }

    private String getTemplateKeyForStatus(com.rajat.quickpick.enums.OrderStatus status) {
        switch (status) {
            case ACCEPTED: return "ORDER_ACCEPTED";
            case REJECTED: return "ORDER_REJECTED";
            case PREPARING: return "ORDER_PREPARING";
            case READY_FOR_PICKUP: return "ORDER_READY";
            case COMPLETED: return "ORDER_COMPLETED";
            case CANCELLED: return "ORDER_CANCELLED";
            default: return "ORDER_UPDATE";
        }
    }

    private String getStatusMessage(com.rajat.quickpick.enums.OrderStatus status) {
        switch (status) {
            case ACCEPTED: return "Your order has been accepted!";
            case REJECTED: return "Your order has been rejected";
            case PREPARING: return "Your order is being prepared";
            case READY_FOR_PICKUP: return "Your order is ready for pickup!";
            case COMPLETED: return "Order completed successfully!";
            case CANCELLED: return "Your order has been cancelled";
            default: return "Order status updated";
        }
    }
}