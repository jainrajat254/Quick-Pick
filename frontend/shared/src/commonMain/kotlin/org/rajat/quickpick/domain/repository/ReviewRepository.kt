package org.rajat.quickpick.domain.repository

import org.rajat.quickpick.domain.modal.review.AdminDeleteReviewResponse
import org.rajat.quickpick.domain.modal.review.CheckIfUserHasReviewedAnOrderResponse
import org.rajat.quickpick.domain.modal.review.CreateReviewStudentRequest
import org.rajat.quickpick.domain.modal.review.CreateReviewStudentResponse
import org.rajat.quickpick.domain.modal.review.DeleteReviewStudentResponse
import org.rajat.quickpick.domain.modal.review.GetAllReviewsForAVendorResponse
import org.rajat.quickpick.domain.modal.review.GetLoggedInStudentsReviewsResponse
import org.rajat.quickpick.domain.modal.review.GetReviewByIDResponse
import org.rajat.quickpick.domain.modal.review.GetReviewCountByVendorResponse
import org.rajat.quickpick.domain.modal.review.GetReviewCountOfCurrentUserResponse
import org.rajat.quickpick.domain.modal.review.GetReviewsByVendorSpecificRatingResponse
import org.rajat.quickpick.domain.modal.review.GetVendorRatingStatsResponse
import org.rajat.quickpick.domain.modal.review.UpdateReviewStudentResponse
import org.rajat.quickpick.domain.modal.review.getPaginatedReviewsforVendor.GetPaginatedReviewsForVendorResponse

interface ReviewRepository {

    suspend fun createReview(createReviewStudentRequest: CreateReviewStudentRequest): Result<CreateReviewStudentResponse>
    suspend fun getReviewById(reviewId: String): Result<GetReviewByIDResponse>
    suspend fun getReviewsByVendor(vendorId: String): Result<GetAllReviewsForAVendorResponse>
    suspend fun getReviewsByVendorPaginated(vendorId: String, page: Int, size: Int): Result<GetPaginatedReviewsForVendorResponse>
    suspend fun getMyReviews(): Result<GetLoggedInStudentsReviewsResponse>
    suspend fun getReviewsByRating(vendorId: String, rating: Int): Result<GetReviewsByVendorSpecificRatingResponse>
    suspend fun getReviewsByRatingRange(vendorId: String, minRating: Int): Result<GetReviewsByVendorSpecificRatingResponse>
    suspend fun getVendorRating(vendorId: String): Result<GetVendorRatingStatsResponse>
    suspend fun updateReview(reviewId: String, createReviewStudentRequest: CreateReviewStudentRequest): Result<UpdateReviewStudentResponse>
    suspend fun deleteReview(reviewId: String): Result<DeleteReviewStudentResponse>
    suspend fun deleteReviewAdmin(reviewId: String): Result<AdminDeleteReviewResponse>
    suspend fun getReviewCountByVendor(vendorId: String): Result<GetReviewCountByVendorResponse>
    suspend fun getMyReviewCount(): Result<GetReviewCountOfCurrentUserResponse>
    suspend fun hasUserReviewedOrder(orderId: String): Result<CheckIfUserHasReviewedAnOrderResponse>
}