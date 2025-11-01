package com.rajat.quickpick.controller;

import com.rajat.quickpick.service.DeletionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/deletion")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class DeletionController {

    private final DeletionService deletionService;


    @DeleteMapping("/vendors/all")
    public ResponseEntity<Map<String, Object>> deleteAllVendors() {
        Map<String, Object> result = deletionService.deleteAllVendors();
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/vendors/email/{email}")
    public ResponseEntity<Map<String, Object>> deleteVendorByEmail(@PathVariable String email) {
        Map<String, Object> result = deletionService.deleteVendorByEmail(email);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/vendors/college/{collegeName}")
    public ResponseEntity<Map<String, Object>> deleteVendorsByCollege(@PathVariable String collegeName) {
        Map<String, Object> result = deletionService.deleteVendorsByCollege(collegeName);
        return ResponseEntity.ok(result);
    }


    @DeleteMapping("/users/all")
    public ResponseEntity<Map<String, Object>> deleteAllUsers() {
        Map<String, Object> result = deletionService.deleteAllUsers();
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/users/email/{email}")
    public ResponseEntity<Map<String, Object>> deleteUserByEmail(@PathVariable String email) {
        Map<String, Object> result = deletionService.deleteUserByEmail(email);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/users/college/{collegeName}")
    public ResponseEntity<Map<String, Object>> deleteUsersByCollege(@PathVariable String collegeName) {
        Map<String, Object> result = deletionService.deleteUsersByCollege(collegeName);
        return ResponseEntity.ok(result);
    }


    @DeleteMapping("/menu-items/all")
    public ResponseEntity<Map<String, Object>> deleteAllMenuItems() {
        Map<String, Object> result = deletionService.deleteAllMenuItems();
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/menu-items/vendor/{vendorEmail}")
    public ResponseEntity<Map<String, Object>> deleteMenuItemsByVendor(@PathVariable String vendorEmail) {
        Map<String, Object> result = deletionService.deleteMenuItemsByVendor(vendorEmail);
        return ResponseEntity.ok(result);
    }


    @DeleteMapping("/orders/all")
    public ResponseEntity<Map<String, Object>> deleteAllOrders() {
        Map<String, Object> result = deletionService.deleteAllOrders();
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/orders/vendor/{vendorEmail}")
    public ResponseEntity<Map<String, Object>> deleteOrdersByVendor(@PathVariable String vendorEmail) {
        Map<String, Object> result = deletionService.deleteOrdersByVendor(vendorEmail);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/orders/user/{userEmail}")
    public ResponseEntity<Map<String, Object>> deleteOrdersByUser(@PathVariable String userEmail) {
        Map<String, Object> result = deletionService.deleteOrdersByUser(userEmail);
        return ResponseEntity.ok(result);
    }


    @DeleteMapping("/reviews/all")
    public ResponseEntity<Map<String, Object>> deleteAllReviews() {
        Map<String, Object> result = deletionService.deleteAllReviews();
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/reviews/vendor/{vendorEmail}")
    public ResponseEntity<Map<String, Object>> deleteReviewsByVendor(@PathVariable String vendorEmail) {
        Map<String, Object> result = deletionService.deleteReviewsByVendor(vendorEmail);
        return ResponseEntity.ok(result);
    }


    @DeleteMapping("/colleges/all")
    public ResponseEntity<Map<String, Object>> deleteAllColleges() {
        Map<String, Object> result = deletionService.deleteAllColleges();
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/colleges/{collegeName}")
    public ResponseEntity<Map<String, Object>> deleteCollegeByName(@PathVariable String collegeName) {
        Map<String, Object> result = deletionService.deleteCollegeByName(collegeName);
        return ResponseEntity.ok(result);
    }


    @DeleteMapping("/tokens/all")
    public ResponseEntity<Map<String, Object>> deleteAllTokens() {
        Map<String, Object> result = deletionService.deleteAllTokens();
        return ResponseEntity.ok(result);
    }


    @DeleteMapping("/database/all")
    public ResponseEntity<Map<String, Object>> deleteAllData(
            @RequestParam(required = true) String confirm) {

        if (!"DELETE_ALL_DATA".equals(confirm)) {
            return ResponseEntity.badRequest().body(
                Map.of("error", "Must provide confirm=DELETE_ALL_DATA to proceed")
            );
        }
        Map<String, Object> result = deletionService.deleteAllData();
        return ResponseEntity.ok(result);
    }

}

