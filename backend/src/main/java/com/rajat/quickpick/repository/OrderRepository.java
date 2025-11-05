package com.rajat.quickpick.repository;

import com.rajat.quickpick.model.Order;
import com.rajat.quickpick.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    List<Order> findByUserId(String userId);

    List<Order> findByVendorId(String vendorId);

    Page<Order> findByVendorId(String vendorId, Pageable pageable);

    List<Order> findByOrderStatus(OrderStatus orderStatus);

    List<Order> findByUserIdAndOrderStatus(String userId, OrderStatus orderStatus);

    List<Order> findByVendorIdAndOrderStatus(String vendorId, OrderStatus orderStatus);

    List<Order> findByUserIdOrderByCreatedAtDesc(String userId);

    List<Order> findByVendorIdOrderByCreatedAtDesc(String vendorId);

    List<Order> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Order> findByVendorIdAndCreatedAtBetween(String vendorId, LocalDateTime startDate, LocalDateTime endDate);

    List<Order> findByUserIdAndCreatedAtBetween(String userId, LocalDateTime startDate, LocalDateTime endDate);

    long countByVendorId(String vendorId);

    long countByUserId(String userId);

    long countByVendorIdAndOrderStatus(String vendorId, OrderStatus orderStatus);

    long countByUserIdAndOrderStatus(String userId, OrderStatus orderStatus);

    List<Order> findByVendorIdAndDeliveredToVendor(String vendorId, boolean deliveredToVendor);

    List<Order> findByVendorIdAndDeliveredToVendorOrderByCreatedAtAsc(String vendorId, boolean deliveredToVendor);
}