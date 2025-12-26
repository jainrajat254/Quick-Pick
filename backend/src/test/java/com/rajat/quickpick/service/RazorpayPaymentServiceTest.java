package com.rajat.quickpick.service;

import com.rajat.quickpick.dto.payment.PaymentRequestDto;
import com.rajat.quickpick.dto.payment.PaymentResponseDto;
import com.rajat.quickpick.enums.OrderStatus;
import com.rajat.quickpick.enums.PaymentMethod;
import com.rajat.quickpick.enums.PaymentStatus;
import com.rajat.quickpick.exception.BadRequestException;
import com.rajat.quickpick.model.Order;
import com.rajat.quickpick.model.User;
import com.rajat.quickpick.repository.OrderRepository;
import com.rajat.quickpick.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RazorpayPaymentServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private com.rajat.quickpick.config.RazorpayConfig razorpayConfig;

    @InjectMocks
    private RazorpayPaymentService razorpayPaymentService;

    private Order order;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId("test-user-1");
        user.setEmail("test@example.com");

        order = new Order();
        order.setId("test-order-1");
        order.setUserId("test-user-1");
        order.setVendorId("test-vendor-1");
        order.setTotalAmount(500.0);
        order.setOrderStatus(OrderStatus.ACCEPTED);
        order.setPaymentStatus(null);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    public void testProcessPayOnDelivery_ShouldSucceed() {
        when(orderRepository.findById("test-order-1")).thenReturn(Optional.of(order));
        when(userRepository.findById("test-user-1")).thenReturn(Optional.of(user));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        PaymentRequestDto request = new PaymentRequestDto();
        request.setOrderId("test-order-1");
        request.setPaymentMethod(PaymentMethod.PAY_ON_DELIVERY);

        PaymentResponseDto response = razorpayPaymentService.initiatePayment("test-user-1", request);

        assertNotNull(response);
        assertEquals(PaymentMethod.PAY_ON_DELIVERY, response.getPaymentMethod());
        assertEquals(PaymentStatus.PENDING_ON_DELIVERY, response.getPaymentStatus());
        assertEquals("SUCCESS", response.getResponseCode());
    }

    @Test
    public void testRefundPayment_WithUnpaidOrder_ShouldFail() {
        order.setPaymentStatus(PaymentStatus.PENDING);
        when(orderRepository.findById("test-order-1")).thenReturn(Optional.of(order));

        assertThrows(BadRequestException.class, () -> {
            razorpayPaymentService.refundPayment("test-order-1", "User requested refund");
        });
    }

    @Test
    public void testRefundPayment_WithPaidOrder_ShouldSucceed() {
        order.setPaymentStatus(PaymentStatus.PAID);
        order.setTransactionId("test-txn-123");
        order.setAmountPaid(500.0);

        when(orderRepository.findById("test-order-1")).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        assertDoesNotThrow(() -> {
            razorpayPaymentService.refundPayment("test-order-1", "User requested refund");
        });

        verify(orderRepository, atLeast(2)).save(any(Order.class));
    }
}

