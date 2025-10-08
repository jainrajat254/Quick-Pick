package com.rajat.quickpick.controller;

import com.rajat.quickpick.dto.review.CreateReviewDto;
import com.rajat.quickpick.dto.review.ReviewResponseDto;
import com.rajat.quickpick.dto.vendor.VendorRatingDto;
import com.rajat.quickpick.security.JwtUtil;
import com.rajat.quickpick.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ReviewResponseDto> createReview(@Valid @RequestBody CreateReviewDto createDto,
                                                          HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        ReviewResponseDto review = reviewService.createReview(userId, createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> getReviewById(@PathVariable String reviewId) {
        ReviewResponseDto review = reviewService.getReviewById(reviewId);
        return ResponseEntity.ok(review);
    }

    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByVendor(@PathVariable String vendorId) {
        List<ReviewResponseDto> reviews = reviewService.getReviewsByVendor(vendorId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/vendor/{vendorId}/paginated")
    public ResponseEntity<Page<ReviewResponseDto>> getReviewsByVendorPaginated(
            @PathVariable String vendorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ReviewResponseDto> reviews = reviewService.getReviewsByVendorPaginated(vendorId, page, size);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/user/my-reviews")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<ReviewResponseDto>> getMyReviews(HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        List<ReviewResponseDto> reviews = reviewService.getReviewsByUser(userId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/vendor/{vendorId}/rating/{rating}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByRating(@PathVariable String vendorId,
                                                                      @PathVariable int rating) {
        List<ReviewResponseDto> reviews = reviewService.getReviewsByRating(vendorId, rating);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/vendor/{vendorId}/rating-above/{minRating}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByRatingRange(@PathVariable String vendorId,
                                                                           @PathVariable int minRating) {
        List<ReviewResponseDto> reviews = reviewService.getReviewsByRatingRange(vendorId, minRating);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/vendor/{vendorId}/statistics")
    public ResponseEntity<VendorRatingDto> getVendorRating(@PathVariable String vendorId) {
        VendorRatingDto rating = reviewService.getVendorRating(vendorId);
        return ResponseEntity.ok(rating);
    }



    @PutMapping("/{reviewId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ReviewResponseDto> updateReview(@PathVariable String reviewId,
                                                          @Valid @RequestBody CreateReviewDto updateDto,
                                                          HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        ReviewResponseDto updatedReview = reviewService.updateReview(userId, reviewId, updateDto);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Map<String, String>> deleteReview(@PathVariable String reviewId,
                                                            HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        reviewService.deleteReview(userId, reviewId);
        return ResponseEntity.ok(Map.of("message", "Review deleted successfully"));
    }

    @DeleteMapping("/admin/{reviewId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteReviewAdmin(@PathVariable String reviewId) {
        reviewService.deleteReviewAdmin(reviewId);
        return ResponseEntity.ok(Map.of("message", "Review deleted by admin successfully"));
    }

    @GetMapping("/vendor/{vendorId}/count")
    public ResponseEntity<Map<String, Long>> getReviewCountByVendor(@PathVariable String vendorId) {
        long count = reviewService.getReviewCountByVendor(vendorId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @GetMapping("/user/count")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Map<String, Long>> getMyReviewCount(HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        long count = reviewService.getReviewCountByUser(userId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @GetMapping("/order/{orderId}/exists")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Map<String, Boolean>> hasUserReviewedOrder(@PathVariable String orderId,
                                                                     HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        boolean hasReviewed = reviewService.hasUserReviewedOrder(userId, orderId);
        return ResponseEntity.ok(Map.of("hasReviewed", hasReviewed));
    }

    private String extractUserIdFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtUtil.extractUserId(token);
        }
        throw new RuntimeException("Invalid token");
    }
}