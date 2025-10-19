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

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

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


        messagingTemplate.convertAndSendToUser(
                vendor.getId(),
                "/queue/orders",
                notification
        );

        log.info("Sent new order notification to vendor: {}", vendor.getId());
    }

    public void notifyOrderStatusUpdate(Order order, User user) {
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
            messagingTemplate.convertAndSendToUser(
                    user.getId(),
                    "/queue/orders",
                    notification
            );

            log.info("Sent order status update to user: {} for order: {}", user.getId(), order.getId());
        }
    }

    public void notifyOrderCancellation(Order order, Vendor vendor) {
        OrderNotificationDto notification = OrderNotificationDto.builder()
                .orderId(order.getId())
                .type("ORDER_CANCELLED")
                .title("Order Cancelled")
                .message("Order #" + order.getId().substring(0, 8) + " has been cancelled by the customer")
                .orderStatus(order.getOrderStatus())
                .totalAmount(order.getTotalAmount())
                .timestamp(order.getUpdatedAt())
                .build();

        messagingTemplate.convertAndSendToUser(
                vendor.getId(),
                "/queue/orders",
                notification
        );

        log.info("Sent order cancellation notification to vendor: {}", vendor.getId());
    }

    private String getStatusMessage(OrderStatus status) {
        switch (status) {
            case ACCEPTED:
                return "Your order has been accepted and will be prepared soon!";
            case REJECTED:
                return "Sorry, your order has been rejected by the vendor.";
            case PREPARING:
                return "Your order is being prepared. Please wait!";
            case READY_FOR_PICKUP:
                return "Your order is ready for pickup!";
            case COMPLETED:
                return "Order completed successfully. Thank you!";
            case CANCELLED:
                return "Your order has been cancelled.";
            default:
                return "Order status updated.";
        }
    }
}