package com.rajat.quickpick.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rajat.quickpick.config.RazorpayConfig;
import com.rajat.quickpick.dto.payment.PaymentRequestDto;
import com.rajat.quickpick.dto.payment.PaymentResponseDto;
import com.rajat.quickpick.dto.payment.PaymentStatusDto;
import com.rajat.quickpick.enums.OrderStatus;
import com.rajat.quickpick.enums.PaymentMethod;
import com.rajat.quickpick.enums.PaymentStatus;
import com.rajat.quickpick.exception.BadRequestException;
import com.rajat.quickpick.exception.ResourceNotFoundException;
import com.rajat.quickpick.model.Order;
import com.rajat.quickpick.model.User;
import com.rajat.quickpick.repository.OrderRepository;
import com.rajat.quickpick.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RazorpayPaymentService {

    private final RazorpayConfig razorpayConfig;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PaymentResponseDto initiatePayment(String userId, PaymentRequestDto paymentRequest) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Order order = orderRepository.findById(paymentRequest.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getUserId().equals(userId)) {
            throw new BadRequestException("Order does not belong to this user");
        }

        if (!(order.getOrderStatus() == OrderStatus.ACCEPTED
                || order.getOrderStatus() == OrderStatus.PREPARING
                || order.getOrderStatus() == OrderStatus.READY_FOR_PICKUP)) {
            throw new BadRequestException(
                    "Payment can only be initiated for ACCEPTED, PREPARING or READY_FOR_PICKUP orders. Current status: " + order.getOrderStatus());
        }

        if (paymentRequest.getPaymentMethod() == PaymentMethod.PAY_NOW) {
            log.info("Initiating Razorpay payment for order: {} with amount: {}", order.getId(), order.getTotalAmount());
            return initiateStandardPayment(order);
        } else if (paymentRequest.getPaymentMethod() == PaymentMethod.PAY_ON_DELIVERY) {
            log.info("Processing PAY_ON_DELIVERY for order: {}", order.getId());
            return processPayOnDelivery(order);
        } else {
            throw new BadRequestException("Invalid payment method");
        }
    }

    private PaymentResponseDto initiateStandardPayment(Order order) {
        try {
            order.setPaymentStatus(PaymentStatus.PAYMENT_INITIATED);
            order.setPaymentMethod(PaymentMethod.PAY_NOW);
            order.setUpdatedAt(LocalDateTime.now());

            Map<String, Object> body = new HashMap<>();
            long amountInPaise = Math.round(order.getTotalAmount() * 100);
            body.put("amount", amountInPaise);
            body.put("currency", "INR");
            body.put("receipt", order.getId());
            body.put("payment_capture", 1);

            WebClient client = webClientBuilder.build();

            Map response = client.post()
                    .uri(razorpayConfig.getApiUrl() + "/orders")
                    .headers(h -> h.setBasicAuth(razorpayConfig.getKeyId(), razorpayConfig.getKeySecret()))
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response != null && response.get("id") != null) {
                String razorpayOrderId = response.get("id").toString();
                order.setTransactionId(razorpayOrderId);
                orderRepository.save(order);

                PaymentResponseDto dto = new PaymentResponseDto();
                dto.setOrderId(order.getId());
                dto.setPaymentMethod(PaymentMethod.PAY_NOW);
                dto.setPaymentStatus(PaymentStatus.PAYMENT_INITIATED);
                dto.setAmount(order.getTotalAmount());
                dto.setResponseCode("SUCCESS");
                dto.setMessage("Razorpay order created");
                dto.setPaymentUrl("");
                dto.setTransactionId(razorpayOrderId);
                dto.setCreatedAt(LocalDateTime.now());
                dto.setRazorpayKeyId(razorpayConfig.getKeyId());
                return dto;
            }

            order.setPaymentStatus(PaymentStatus.PAYMENT_FAILED);
            orderRepository.save(order);
            throw new BadRequestException("Failed to create Razorpay order");

        } catch (WebClientResponseException e) {
            log.error("Razorpay API error for order {}: {}", order.getId(), e.getResponseBodyAsString());
            throw new BadRequestException("Razorpay API error: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error initiating Razorpay payment for order: {}", order.getId(), e);
            order.setPaymentStatus(PaymentStatus.PAYMENT_FAILED);
            orderRepository.save(order);
            throw new BadRequestException("Payment initiation failed: " + e.getMessage());
        }
    }

    private PaymentResponseDto processPayOnDelivery(Order order) {
        order.setPaymentStatus(PaymentStatus.PENDING_ON_DELIVERY);
        order.setPaymentMethod(PaymentMethod.PAY_ON_DELIVERY);
        order.setUpdatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);
        log.info("Order {} marked for pay on delivery", order.getId());

        PaymentResponseDto response = new PaymentResponseDto();
        response.setOrderId(savedOrder.getId());
        response.setPaymentMethod(PaymentMethod.PAY_ON_DELIVERY);
        response.setPaymentStatus(PaymentStatus.PENDING_ON_DELIVERY);
        response.setAmount(savedOrder.getTotalAmount());
        response.setMessage("Order confirmed for payment on delivery");
        response.setResponseCode("SUCCESS");
        response.setCreatedAt(LocalDateTime.now());

        return response;
    }

    public void handleRazorpayWebhook(String payload, String signature) {
        try {
            String secret = razorpayConfig.getWebhookSecret();
            if (secret == null || secret.isEmpty()) {
                log.warn("No webhook secret configured; skipping signature verification");
            } else {
                String expected = calculateHmacSHA256(payload, secret);
                if (!safeEquals(expected, signature)) {
                    log.warn("Invalid Razorpay webhook signature. expected={}, received={}", expected, signature);
                    throw new BadRequestException("Invalid webhook signature");
                }
            }

            JsonNode root = objectMapper.readTree(payload);
            String event = root.has("event") ? root.get("event").asText() : null;
            if (event == null) {
                log.warn("Webhook payload missing event field");
                return;
            }

            if (event.startsWith("payment.")) {
                JsonNode payloadNode = root.path("payload").path("payment");
                JsonNode entity = payloadNode.path("entity");
                String status = entity.path("status").asText();
                String paymentId = entity.path("id").asText();
                long amount = entity.path("amount").asLong();
                String orderId = entity.path("order_id").asText();

                Order order = orderRepository.findByTransactionId(orderId);
                if (order == null) {
                    log.warn("No order found for transaction/order id: {}", orderId);
                    return;
                }

                if ("captured".equalsIgnoreCase(status)) {
                    order.setPaymentStatus(PaymentStatus.PAID);
                    order.setTransactionId(paymentId);
                    order.setAmountPaid(amount / 100.0);
                    order.setPaymentDate(LocalDateTime.now());
                    order.setUpdatedAt(LocalDateTime.now());
                    orderRepository.save(order);
                    log.info("Payment captured for order: {}", order.getId());
                } else if ("failed".equalsIgnoreCase(status)) {
                    order.setPaymentStatus(PaymentStatus.PAYMENT_FAILED);
                    order.setUpdatedAt(LocalDateTime.now());
                    orderRepository.save(order);
                    log.warn("Payment failed for order: {}", order.getId());
                }
            } else {
                log.debug("Unhandled Razorpay webhook event: {}", event);
            }

        } catch (BadRequestException bre) {
            throw bre;
        } catch (Exception e) {
            log.error("Error processing Razorpay webhook", e);
            throw new BadRequestException("Webhook processing failed: " + e.getMessage());
        }
    }

    private String calculateHmacSHA256(String data, String secret) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] hash = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    private boolean safeEquals(String a, String b) {
        if (a == null || b == null) return false;
        return a.equals(b);
    }

    public PaymentStatusDto checkPaymentStatus(String userId, String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getUserId().equals(userId)) {
            throw new BadRequestException("Order does not belong to this user");
        }

        PaymentStatusDto statusDto = new PaymentStatusDto();
        statusDto.setOrderId(orderId);
        statusDto.setPaymentStatus(order.getPaymentStatus());
        statusDto.setPaymentMethod(order.getPaymentMethod());
        statusDto.setTransactionId(order.getTransactionId());
        statusDto.setAmountPaid(order.getAmountPaid());

        boolean isPaymentAvailable = (order.getOrderStatus() == OrderStatus.ACCEPTED
                || order.getOrderStatus() == OrderStatus.PREPARING
                || order.getOrderStatus() == OrderStatus.READY_FOR_PICKUP)
                && (order.getPaymentStatus() == null
                || order.getPaymentStatus() == PaymentStatus.PENDING
                || order.getPaymentStatus() == PaymentStatus.PAYMENT_INITIATED);
        statusDto.setPaymentAvailable(isPaymentAvailable);

        if (order.getPaymentStatus() == PaymentStatus.PAID) {
            statusDto.setMessage("Payment completed successfully");
        } else if (order.getPaymentStatus() == PaymentStatus.PAYMENT_FAILED) {
            statusDto.setMessage("Payment failed. Please try again");
        } else if (order.getPaymentStatus() == PaymentStatus.PENDING_ON_DELIVERY) {
            statusDto.setMessage("Payment pending on delivery");
        } else {
            statusDto.setMessage("Payment pending");
        }

        log.debug("Payment status for order {}: {}", orderId, order.getPaymentStatus());
        return statusDto;
    }

    public void refundPayment(String orderId, String reason) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getPaymentStatus() != PaymentStatus.PAID) {
            throw new BadRequestException(
                    "Can only refund paid orders. Current payment status: " + order.getPaymentStatus());
        }

        try {
            order.setPaymentStatus(PaymentStatus.REFUND_INITIATED);
            order.setUpdatedAt(LocalDateTime.now());
            orderRepository.save(order);

            log.info("Refund initiated for order: {} with reason: {}", orderId, reason);

            order.setPaymentStatus(PaymentStatus.REFUND_COMPLETED);
            order.setUpdatedAt(LocalDateTime.now());
            orderRepository.save(order);

            log.info("Refund completed for order: {}", orderId);

        } catch (Exception e) {
            log.error("Error processing refund for order: {}", orderId, e);
            order.setPaymentStatus(PaymentStatus.PAID);
            orderRepository.save(order);
            throw new BadRequestException("Refund processing failed: " + e.getMessage());
        }
    }
}

