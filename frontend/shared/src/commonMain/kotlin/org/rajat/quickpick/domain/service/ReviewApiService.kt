package org.rajat.quickpick.domain.service

import org.rajat.quickpick.domain.modal.review.*
import org.rajat.quickpick.domain.modal.review.getPaginatedReviewsforVendor.GetPaginatedReviewsForVendorResponse

interface ReviewApiService {

    suspend fun createReview(createReviewStudentRequest: CreateReviewStudentRequest): CreateReviewStudentResponse
    suspend fun getReviewById(reviewId: String): GetReviewByIDResponse
    suspend fun getReviewsByVendor(vendorId: String): GetAllReviewsForAVendorResponse
    suspend fun getReviewsByVendorPaginated(vendorId: String, page: Int, size: Int): GetPaginatedReviewsForVendorResponse
    suspend fun getMyReviews(): GetLoggedInStudentsReviewsResponse
    suspend fun getReviewsByRating(vendorId: String, rating: Int): GetReviewsByVendorSpecificRatingResponse
    suspend fun getReviewsByRatingRange(vendorId: String, minRating: Int): GetReviewsByVendorSpecificRatingResponse
    suspend fun getVendorRating(vendorId: String): GetVendorRatingStatsResponse
    suspend fun updateReview(reviewId: String, createReviewStudentRequest: CreateReviewStudentRequest): UpdateReviewStudentResponse
    suspend fun deleteReview(reviewId: String): DeleteReviewStudentResponse
    suspend fun deleteReviewAdmin(reviewId: String): AdminDeleteReviewResponse
    suspend fun getReviewCountByVendor(vendorId: String): GetReviewCountByVendorResponse
    suspend fun getMyReviewCount(): GetReviewCountOfCurrentUserResponse
    suspend fun hasUserReviewedOrder(orderId: String): CheckIfUserHasReviewedAnOrderResponse
}