package com.rajat.quickpick.service;

import com.rajat.quickpick.enums.OrderStatus;
import com.rajat.quickpick.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final FcmService fcmService;

    public void sendOrderNotification(Order order, String templateKey, String recipientId, String recipientType) {
        Map<String, String> notificationData = getNotificationData(order, templateKey);

        String title = notificationData.get("title");
        String body = notificationData.get("body");

        Map<String, String> data = new HashMap<>();
        data.put("orderId", order.getId());
        data.put("orderStatus", order.getOrderStatus().name());
        data.put("totalAmount", String.valueOf(order.getTotalAmount()));
        data.put("type", templateKey);

        if ("ORDER_ACCEPTED".equals(templateKey) && order.getOtp() != null) {
            data.put("otp", order.getOtp());
        }

        fcmService.sendNotification(recipientId, title, body, data);
    }

    private Map<String, String> getNotificationData(Order order, String templateKey) {
        Map<String, String> data = new HashMap<>();
        String orderShortId = order.getId().substring(0, 8);

        switch (templateKey) {
            case "ORDER_CREATED":
                data.put("title", "🔔 New Order Received!");
                data.put("body", "Order #" + orderShortId + " - ₹" + order.getTotalAmount());
                break;

            case "ORDER_ACCEPTED":
                data.put("title", "✅ Order Accepted!");
                data.put("body", "Your order #" + orderShortId + " has been accepted");
                break;

            case "ORDER_REJECTED":
                data.put("title", "❌ Order Rejected");
                data.put("body", "Sorry, order #" + orderShortId + " was rejected");
                break;

            case "ORDER_PREPARING":
                data.put("title", "👨‍🍳 Preparing Your Order");
                data.put("body", "Order #" + orderShortId + " is being prepared");
                break;

            case "ORDER_READY":
                data.put("title", "✨ Order Ready!");
                data.put("body", "Order #" + orderShortId + " is ready for pickup");
                break;

            case "ORDER_COMPLETED":
                data.put("title", "🎉 Order Completed");
                data.put("body", "Thank you! Order #" + orderShortId + " completed");
                break;

            case "ORDER_CANCELLED":
                data.put("title", "🚫 Order Cancelled");
                data.put("body", "Order #" + orderShortId + " has been cancelled");
                break;

            default:
                data.put("title", "Order Update");
                data.put("body", "Order #" + orderShortId + " status updated");
        }

        return data;
    }
}