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
        log.info("order notification new order to vendor");
        log.info("order notif orderid {}", order.getId());
        log.info("order notif vendorid {}", vendor.getId());
        log.info("order notif vendor email {}", vendor.getEmail());
        log.info("order notif order status {}", order.getOrderStatus());
        log.info("order notif total amount {}", order.getTotalAmount());

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
        boolean isVendorConnected = connectionManager.isUserConnected(vendor.getId());

        log.info("order notif vendor websocket connected {}", isVendorConnected);

        if (isVendorConnected) {
            try {
                log.info("order notif attempting to send via websocket to vendor {}", vendor.getId());
                messagingTemplate.convertAndSendToUser(
                        vendor.getId(),
                        "/queue/orders",
                        notification
                );

                sentViaWebSocket = true;
                log.info("order notif sent new order notification via websocket to vendor {}", vendor.getId());
            } catch (Exception e) {
                log.error("order notif failed to send websocket notification to vendor {}", vendor.getId(), e);
            }
        }

        if (!sentViaWebSocket) {
            log.info("order notif vendor {} is offline or websocket failed sending fcm notification", vendor.getId());
            notificationService.sendOrderNotification(order, "ORDER_CREATED", vendor.getId(), "VENDOR");
        } else {
            log.info("order notif websocket successful skipping fcm notification");
        }

        log.info("order notification new order complete");
    }

    public void notifyOrderStatusUpdate(Order order, User user) {
        log.info("order notification status update to user");
        log.info("order notif orderid {}", order.getId());
        log.info("order notif userid {}", user != null ? user.getId() : "NULL");
        log.info("order notif user email {}", user != null ? user.getEmail() : "NULL");
        log.info("order notif new status {}", order.getOrderStatus());

        String templateKey = getTemplateKeyForStatus(order.getOrderStatus());
        String message = getStatusMessage(order.getOrderStatus());

        log.info("order notif template key {}", templateKey);
        log.info("order notif message {}", message);

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
            boolean isUserConnected = connectionManager.isUserConnected(user.getId());
            log.info("order notif user websocket connected {}", isUserConnected);

            if (isUserConnected) {
                try {
                    log.info("order notif attempting to send via websocket to user {}", user.getId());
                    messagingTemplate.convertAndSendToUser(
                            user.getId(),
                            "/queue/orders",
                            notification
                    );
                    log.info("order notif sent order status update via websocket to user {}", user.getId());
                } catch (Exception e) {
                    log.error("order notif failed to send websocket notification to user {}", user.getId(), e);
                }
            } else {
                log.info("order notif user is not connected via websocket");
            }

            // Always send FCM for important status updates
            log.info("order notif sending fcm notification for status update");
            notificationService.sendOrderNotification(order, templateKey, user.getId(), "USER");
        } else {
            log.warn("order notif user is null cannot send notification");
        }

        log.info("order notification status update complete");
    }

    public void notifyOrderCancellation(Order order, Vendor vendor) {
        log.info("order notification cancellation to vendor");
        log.info("order notif orderid {}", order.getId());
        log.info("order notif vendorid {}", vendor.getId());
        log.info("order notif vendor email {}", vendor.getEmail());

        OrderNotificationDto notification = OrderNotificationDto.builder()
                .orderId(order.getId())
                .type("ORDER_CANCELLED")
                .title("Order Cancelled")
                .message("Order #" + order.getId().substring(0, 8) + " has been cancelled")
                .orderStatus(order.getOrderStatus())
                .totalAmount(order.getTotalAmount())
                .timestamp(order.getUpdatedAt())
                .build();

        boolean isVendorConnected = connectionManager.isUserConnected(vendor.getId());
        log.info("order notif vendor websocket connected {}", isVendorConnected);

        if (isVendorConnected) {
            try {
                log.info("order notif attempting to send via websocket to vendor {}", vendor.getId());
                messagingTemplate.convertAndSendToUser(
                        vendor.getId(),
                        "/queue/orders",
                        notification
                );
                log.info("order notif sent order cancellation via websocket to vendor {}", vendor.getId());
            } catch (Exception e) {
                log.error("order notif failed to send websocket notification to vendor {}", vendor.getId(), e);
            }
        }

        log.info("order notif sending fcm notification for cancellation");
        notificationService.sendOrderNotification(order, "ORDER_CANCELLED", vendor.getId(), "VENDOR");

        log.info("order notification cancellation complete");
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