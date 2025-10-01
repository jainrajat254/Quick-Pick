package com.rajat.quickpick.repository;

import com.rajat.quickpick.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {

    List<Review> findByVendorId(String vendorId);

    List<Review> findByUserId(String userId);

    Optional<Review> findByUserIdAndOrderId(String userId, String orderId);

    boolean existsByUserIdAndOrderId(String userId, String orderId);

    List<Review> findByVendorIdOrderByCreatedAtDesc(String vendorId);

    List<Review> findByUserIdOrderByCreatedAtDesc(String userId);

    List<Review> findByRating(int rating);

    List<Review> findByVendorIdAndRating(String vendorId, int rating);

    List<Review> findByRatingGreaterThanEqual(int rating);

    List<Review> findByVendorIdAndRatingGreaterThanEqual(String vendorId, int rating);

    @Query("{ 'vendorId': ?0 }")
    List<Review> findAllReviewsForVendor(String vendorId);

    @Query(value = "{ 'vendorId': ?0 }", fields = "{ 'rating': 1 }")
    List<Review> findRatingsByVendorId(String vendorId);

    long countByVendorId(String vendorId);

    long countByVendorIdAndRating(String vendorId, int rating);
}