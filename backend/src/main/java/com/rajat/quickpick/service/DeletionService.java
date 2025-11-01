package com.rajat.quickpick.service;

import com.rajat.quickpick.model.*;
import com.rajat.quickpick.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeletionService {

    private final VendorRepository vendorRepository;
    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;
    private final CollegeRepository collegeRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;


    @Transactional
    public Map<String, Object> deleteAllVendors() {
        log.warn("Deleting all vendors");
        long count = vendorRepository.count();

        List<Vendor> vendors = vendorRepository.findAll();
        for (Vendor vendor : vendors) {
            deleteVendorRelatedData(vendor.getId());
        }

        vendorRepository.deleteAll();

        Map<String, Object> result = new HashMap<>();
        result.put("deletedCount", count);
        result.put("message", "All vendors deleted successfully");
        return result;
    }

    @Transactional
    public Map<String, Object> deleteVendorByEmail(String email) {
        Vendor vendor = vendorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Vendor not found with email: " + email));

        deleteVendorRelatedData(vendor.getId());
        vendorRepository.delete(vendor);

        Map<String, Object> result = new HashMap<>();
        result.put("deletedEmail", email);
        result.put("message", "Vendor deleted successfully");
        return result;
    }

    @Transactional
    public Map<String, Object> deleteVendorsByCollege(String collegeName) {
        List<Vendor> vendors = vendorRepository.findByCollegeName(collegeName);

        for (Vendor vendor : vendors) {
            deleteVendorRelatedData(vendor.getId());
        }

        int count = vendors.size();
        vendorRepository.deleteAll(vendors);

        Map<String, Object> result = new HashMap<>();
        result.put("deletedCount", count);
        result.put("collegeName", collegeName);
        result.put("message", "Vendors deleted successfully");
        return result;
    }

    private void deleteVendorRelatedData(String vendorId) {
        List<MenuItem> menuItems = menuItemRepository.findByVendorId(vendorId);
        menuItemRepository.deleteAll(menuItems);
        log.debug("Deleted {} menu items for vendor: {}", menuItems.size(), vendorId);

        List<Review> reviews = reviewRepository.findByVendorId(vendorId);
        reviewRepository.deleteAll(reviews);
        log.debug("Deleted {} reviews for vendor: {}", reviews.size(), vendorId);


        // List<Order> orders = orderRepository.findByVendorId(vendorId);
        // orderRepository.deleteAll(orders);
    }


    @Transactional
    public Map<String, Object> deleteAllUsers() {
        log.warn("Deleting all users");
        long count = userRepository.count();

        List<User> users = userRepository.findAll();
        for (User user : users) {
            deleteUserRelatedData(user.getId());
        }

        userRepository.deleteAll();
        log.info("Deleted {} users and their related data", count);

        Map<String, Object> result = new HashMap<>();
        result.put("deletedCount", count);
        result.put("message", "All users deleted successfully");
        return result;
    }

    @Transactional
    public Map<String, Object> deleteUserByEmail(String email) {
        log.warn("Deleting user with email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        deleteUserRelatedData(user.getId());
        userRepository.delete(user);

        log.info("Deleted user: {}", email);

        Map<String, Object> result = new HashMap<>();
        result.put("deletedEmail", email);
        result.put("message", "User deleted successfully");
        return result;
    }

    @Transactional
    public Map<String, Object> deleteUsersByCollege(String collegeName) {
        log.warn("Deleting all users from college: {}", collegeName);
        List<User> users = userRepository.findByCollegeName(collegeName);

        for (User user : users) {
            deleteUserRelatedData(user.getId());
        }

        int count = users.size();
        userRepository.deleteAll(users);

        log.info("Deleted {} users from college: {}", count, collegeName);

        Map<String, Object> result = new HashMap<>();
        result.put("deletedCount", count);
        result.put("collegeName", collegeName);
        result.put("message", "Users deleted successfully");
        return result;
    }

    private void deleteUserRelatedData(String userId) {
        // Delete reviews by this user
        List<Review> reviews = reviewRepository.findByUserId(userId);
        reviewRepository.deleteAll(reviews);
        log.debug("Deleted {} reviews for user: {}", reviews.size(), userId);

        refreshTokenRepository.deleteByUserId(userId);

    }


    @Transactional
    public Map<String, Object> deleteAllMenuItems() {
        log.warn("Deleting all menu items");
        long count = menuItemRepository.count();
        menuItemRepository.deleteAll();

        log.info("Deleted {} menu items", count);

        Map<String, Object> result = new HashMap<>();
        result.put("deletedCount", count);
        result.put("message", "All menu items deleted successfully");
        return result;
    }

    @Transactional
    public Map<String, Object> deleteMenuItemsByVendor(String vendorEmail) {
        log.warn("Deleting all menu items for vendor: {}", vendorEmail);
        Vendor vendor = vendorRepository.findByEmail(vendorEmail)
                .orElseThrow(() -> new RuntimeException("Vendor not found with email: " + vendorEmail));

        List<MenuItem> menuItems = menuItemRepository.findByVendorId(vendor.getId());
        int count = menuItems.size();
        menuItemRepository.deleteAll(menuItems);

        log.info("Deleted {} menu items for vendor: {}", count, vendorEmail);

        Map<String, Object> result = new HashMap<>();
        result.put("deletedCount", count);
        result.put("vendorEmail", vendorEmail);
        result.put("message", "Menu items deleted successfully");
        return result;
    }


    @Transactional
    public Map<String, Object> deleteAllOrders() {
        log.warn("Deleting all orders");
        long count = orderRepository.count();
        orderRepository.deleteAll();

        log.info("Deleted {} orders", count);

        Map<String, Object> result = new HashMap<>();
        result.put("deletedCount", count);
        result.put("message", "All orders deleted successfully");
        return result;
    }

    @Transactional
    public Map<String, Object> deleteOrdersByVendor(String vendorEmail) {
        log.warn("Deleting all orders for vendor: {}", vendorEmail);
        Vendor vendor = vendorRepository.findByEmail(vendorEmail)
                .orElseThrow(() -> new RuntimeException("Vendor not found with email: " + vendorEmail));

        List<Order> orders = orderRepository.findByVendorId(vendor.getId());
        int count = orders.size();
        orderRepository.deleteAll(orders);

        log.info("Deleted {} orders for vendor: {}", count, vendorEmail);

        Map<String, Object> result = new HashMap<>();
        result.put("deletedCount", count);
        result.put("vendorEmail", vendorEmail);
        result.put("message", "Orders deleted successfully");
        return result;
    }

    @Transactional
    public Map<String, Object> deleteOrdersByUser(String userEmail) {
        log.warn("Deleting all orders for user: {}", userEmail);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        List<Order> orders = orderRepository.findByUserId(user.getId());
        int count = orders.size();
        orderRepository.deleteAll(orders);

        log.info("Deleted {} orders for user: {}", count, userEmail);

        Map<String, Object> result = new HashMap<>();
        result.put("deletedCount", count);
        result.put("userEmail", userEmail);
        result.put("message", "Orders deleted successfully");
        return result;
    }

    // ==================== REVIEW DELETIONS ====================

    @Transactional
    public Map<String, Object> deleteAllReviews() {
        log.warn("Deleting all reviews");
        long count = reviewRepository.count();
        reviewRepository.deleteAll();

        log.info("Deleted {} reviews", count);

        Map<String, Object> result = new HashMap<>();
        result.put("deletedCount", count);
        result.put("message", "All reviews deleted successfully");
        return result;
    }

    @Transactional
    public Map<String, Object> deleteReviewsByVendor(String vendorEmail) {
        log.warn("Deleting all reviews for vendor: {}", vendorEmail);
        Vendor vendor = vendorRepository.findByEmail(vendorEmail)
                .orElseThrow(() -> new RuntimeException("Vendor not found with email: " + vendorEmail));

        List<Review> reviews = reviewRepository.findByVendorId(vendor.getId());
        int count = reviews.size();
        reviewRepository.deleteAll(reviews);

        log.info("Deleted {} reviews for vendor: {}", count, vendorEmail);

        Map<String, Object> result = new HashMap<>();
        result.put("deletedCount", count);
        result.put("vendorEmail", vendorEmail);
        result.put("message", "Reviews deleted successfully");
        return result;
    }


    @Transactional
    public Map<String, Object> deleteAllColleges() {
        log.warn("Deleting all colleges");
        long count = collegeRepository.count();
        collegeRepository.deleteAll();

        log.info("Deleted {} colleges", count);

        Map<String, Object> result = new HashMap<>();
        result.put("deletedCount", count);
        result.put("message", "All colleges deleted successfully");
        return result;
    }

    @Transactional
    public Map<String, Object> deleteCollegeByName(String collegeName) {
        log.warn("Deleting college: {}", collegeName);
        College college = collegeRepository.findByName(collegeName)
                .orElseThrow(() -> new RuntimeException("College not found: " + collegeName));

        collegeRepository.delete(college);

        log.info("Deleted college: {}", collegeName);

        Map<String, Object> result = new HashMap<>();
        result.put("deletedCollege", collegeName);
        result.put("message", "College deleted successfully");
        return result;
    }


    @Transactional
    public Map<String, Object> deleteAllTokens() {
        log.warn("Deleting all tokens");
        long emailTokens = emailVerificationTokenRepository.count();
        long passwordTokens = passwordResetTokenRepository.count();
        long refreshTokens = refreshTokenRepository.count();

        emailVerificationTokenRepository.deleteAll();
        passwordResetTokenRepository.deleteAll();
        refreshTokenRepository.deleteAll();

        log.info("Deleted {} email verification tokens, {} password reset tokens, {} refresh tokens",
                emailTokens, passwordTokens, refreshTokens);

        Map<String, Object> result = new HashMap<>();
        result.put("emailVerificationTokens", emailTokens);
        result.put("passwordResetTokens", passwordTokens);
        result.put("refreshTokens", refreshTokens);
        result.put("message", "All tokens deleted successfully");
        return result;
    }


    @Transactional
    public Map<String, Object> deleteAllData() {
        log.warn("!!! DELETING ALL DATA FROM DATABASE !!!");

        Map<String, Long> counts = new HashMap<>();
        counts.put("vendors", vendorRepository.count());
        counts.put("users", userRepository.count());
        counts.put("menuItems", menuItemRepository.count());
        counts.put("orders", orderRepository.count());
        counts.put("reviews", reviewRepository.count());
        counts.put("colleges", collegeRepository.count());

        vendorRepository.deleteAll();
        userRepository.deleteAll();
        menuItemRepository.deleteAll();
        orderRepository.deleteAll();
        reviewRepository.deleteAll();
        collegeRepository.deleteAll();
        emailVerificationTokenRepository.deleteAll();
        passwordResetTokenRepository.deleteAll();
        refreshTokenRepository.deleteAll();

        log.warn("!!! ALL DATA DELETED !!!");

        Map<String, Object> result = new HashMap<>();
        result.put("deletedCounts", counts);
        result.put("message", "ALL DATA DELETED FROM DATABASE");
        return result;
    }
}

