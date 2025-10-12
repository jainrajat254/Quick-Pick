package com.rajat.quickpick.service;

import com.rajat.quickpick.dto.review.CreateReviewDto;
import com.rajat.quickpick.dto.review.ReviewResponseDto;
import com.rajat.quickpick.dto.vendor.VendorRatingDto;
import com.rajat.quickpick.enums.OrderStatus;
import com.rajat.quickpick.exception.BadRequestException;
import com.rajat.quickpick.exception.ResourceNotFoundException;
import com.rajat.quickpick.model.Order;
import com.rajat.quickpick.model.Review;
import com.rajat.quickpick.model.User;
import com.rajat.quickpick.model.Vendor;
import com.rajat.quickpick.repository.OrderRepository;
import com.rajat.quickpick.repository.ReviewRepository;
import com.rajat.quickpick.repository.UserRepository;
import com.rajat.quickpick.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private UserRepository userRepository;

    public ReviewResponseDto createReview(String userId, CreateReviewDto createDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Vendor vendor = vendorRepository.findById(createDto.getVendorId())
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

        Order order = orderRepository.findById(createDto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getUserId().equals(userId)) {
            throw new BadRequestException("You can only review your own orders");
        }

        if (!order.getVendorId().equals(createDto.getVendorId())) {
            throw new BadRequestException("Order does not belong to this vendor");
        }

        if (order.getOrderStatus() != OrderStatus.COMPLETED) {
            throw new BadRequestException("You can only review completed orders");
        }

        if (reviewRepository.existsByUserIdAndOrderId(userId, createDto.getOrderId())) {
            throw new BadRequestException("You have already reviewed this order");
        }

        Review review = new Review();
        review.setUserId(userId);
        review.setVendorId(createDto.getVendorId());
        review.setOrderId(createDto.getOrderId());
        review.setRating(createDto.getRating());
        review.setComment(createDto.getComment() != null ? createDto.getComment().trim() : null);
        review.setCreatedAt(LocalDateTime.now());

        Review savedReview = reviewRepository.save(review);
        log.info("Review created for vendor {} by user {}", createDto.getVendorId(), userId);

        clearVendorRatingCache(createDto.getVendorId());

        return mapToResponseDto(savedReview, user.getFullName());
    }

    public ReviewResponseDto getReviewById(String reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        User user = userRepository.findById(review.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return mapToResponseDto(review, user.getFullName());
    }

    public List<ReviewResponseDto> getReviewsByVendor(String vendorId) {
        List<Review> reviews = reviewRepository.findByVendorIdOrderByCreatedAtDesc(vendorId);
        return mapToResponseDtoList(reviews);
    }

    public Page<ReviewResponseDto> getReviewsByVendorPaginated(String vendorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Review> reviewPage = reviewRepository.findByVendorId(vendorId, pageable);

        return reviewPage.map(review -> {
            User user = userRepository.findById(review.getUserId()).orElse(null);
            String userName = user != null ? user.getFullName() : "Unknown User";
            return mapToResponseDto(review, userName);
        });
    }

    public List<ReviewResponseDto> getReviewsByUser(String userId) {
        List<Review> reviews = reviewRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return mapToResponseDtoList(reviews);
    }

    public List<ReviewResponseDto> getReviewsByRating(String vendorId, int rating) {
        List<Review> reviews = reviewRepository.findByVendorIdAndRating(vendorId, rating);
        return mapToResponseDtoList(reviews);
    }
    public List<ReviewResponseDto> getReviewsByRatingRange(String vendorId, int minRating) {
        List<Review> reviews = reviewRepository.findByVendorIdAndRatingGreaterThanEqual(vendorId, minRating);
        return mapToResponseDtoList(reviews);
    }

    @Cacheable(value = "vendorRatings", key = "#vendorId")
    public VendorRatingDto getVendorRating(String vendorId) {
        List<Review> reviews = reviewRepository.findByVendorId(vendorId);

        if (reviews.isEmpty()) {
            return new VendorRatingDto(vendorId, 0.0, 0, null);
        }

        double averageRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        int totalReviews = reviews.size();

        Map<Integer, Long> ratingDistribution = reviews.stream()
                .collect(Collectors.groupingBy(Review::getRating, Collectors.counting()));

        VendorRatingDto ratingDto = new VendorRatingDto();
        ratingDto.setVendorId(vendorId);
        ratingDto.setAverageRating(Math.round(averageRating * 10.0) / 10.0);
        ratingDto.setTotalReviews(totalReviews);
        ratingDto.setRatingDistribution(ratingDistribution);

        return ratingDto;
    }

    public ReviewResponseDto updateReview(String userId, String reviewId, CreateReviewDto updateDto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!review.getUserId().equals(userId)) {
            throw new BadRequestException("You can only update your own reviews");
        }

        review.setRating(updateDto.getRating());
        review.setComment(updateDto.getComment() != null ? updateDto.getComment().trim() : null);

        Review updatedReview = reviewRepository.save(review);
        log.info("Review updated: {}", reviewId);

        clearVendorRatingCache(review.getVendorId());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return mapToResponseDto(updatedReview, user.getFullName());
    }

    public void deleteReview(String userId, String reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!review.getUserId().equals(userId)) {
            throw new BadRequestException("You can only delete your own reviews");
        }

        String vendorId = review.getVendorId();
        reviewRepository.delete(review);
        log.info("Review deleted: {}", reviewId);

        clearVendorRatingCache(vendorId);
    }

    public void deleteReviewAdmin(String reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        String vendorId = review.getVendorId();
        reviewRepository.delete(review);
        log.info("Review deleted by admin: {}", reviewId);

        clearVendorRatingCache(vendorId);
    }

    public long getReviewCountByVendor(String vendorId) {
        return reviewRepository.countByVendorId(vendorId);
    }

    public long getReviewCountByUser(String userId) {
        return reviewRepository.countByUserId(userId);
    }

    public boolean hasUserReviewedOrder(String userId, String orderId) {
        return reviewRepository.existsByUserIdAndOrderId(userId, orderId);
    }

    @CacheEvict(value = "vendorRatings", key = "#vendorId")
    private void clearVendorRatingCache(String vendorId) {
        log.debug("Cleared rating cache for vendor: {}", vendorId);
    }

    private List<ReviewResponseDto> mapToResponseDtoList(List<Review> reviews) {
        return reviews.stream()
                .map(review -> {
                    User user = userRepository.findById(review.getUserId()).orElse(null);
                    String userName = user != null ? user.getFullName() : "Unknown User";
                    return mapToResponseDto(review, userName);
                })
                .collect(Collectors.toList());
    }

    private ReviewResponseDto mapToResponseDto(Review review, String userName) {
        ReviewResponseDto dto = new ReviewResponseDto();
        dto.setId(review.getId());
        dto.setUserId(review.getUserId());
        dto.setUserName(userName);
        dto.setVendorId(review.getVendorId());
        dto.setOrderId(review.getOrderId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        return dto;
    }
}
