package org.rajat.quickpick.data.repository

import org.rajat.quickpick.domain.modal.review.*
import org.rajat.quickpick.domain.modal.review.getPaginatedReviewsforVendor.GetPaginatedReviewsForVendorResponse
import org.rajat.quickpick.domain.repository.ReviewRepository
import org.rajat.quickpick.domain.service.ReviewApiService

class ReviewRepositoryImpl(private val reviewApiService: ReviewApiService) : ReviewRepository {

    override suspend fun createReview(createReviewStudentRequest: CreateReviewStudentRequest): Result<CreateReviewStudentResponse> {
        return runCatching {
            reviewApiService.createReview(createReviewStudentRequest)
        }
    }

    override suspend fun getReviewById(reviewId: String): Result<GetReviewByIDResponse> {
        return runCatching {
            reviewApiService.getReviewById(reviewId)
        }
    }

    override suspend fun getReviewsByVendor(vendorId: String): Result<GetAllReviewsForAVendorResponse> {
        return runCatching {
            reviewApiService.getReviewsByVendor(vendorId)
        }
    }

    override suspend fun getReviewsByVendorPaginated(vendorId: String, page: Int, size: Int): Result<GetPaginatedReviewsForVendorResponse> {
        return runCatching {
            reviewApiService.getReviewsByVendorPaginated(vendorId, page, size)
        }
    }

    override suspend fun getMyReviews(): Result<GetLoggedInStudentsReviewsResponse> {
        return runCatching {
            reviewApiService.getMyReviews()
        }
    }

    override suspend fun getReviewsByRating(vendorId: String, rating: Int): Result<GetReviewsByVendorSpecificRatingResponse> {
        return runCatching {
            reviewApiService.getReviewsByRating(vendorId, rating)
        }
    }

    override suspend fun getReviewsByRatingRange(vendorId: String, minRating: Int): Result<GetReviewsByVendorSpecificRatingResponse> {
        return runCatching {
            reviewApiService.getReviewsByRatingRange(vendorId, minRating)
        }
    }

    override suspend fun getVendorRating(vendorId: String): Result<GetVendorRatingStatsResponse> {
        return runCatching {
            reviewApiService.getVendorRating(vendorId)
        }
    }

    override suspend fun updateReview(reviewId: String, createReviewStudentRequest: CreateReviewStudentRequest): Result<UpdateReviewStudentResponse> {
        return runCatching {
            reviewApiService.updateReview(reviewId, createReviewStudentRequest)
        }
    }

    override suspend fun deleteReview(reviewId: String): Result<DeleteReviewStudentResponse> {
        return runCatching {
            reviewApiService.deleteReview(reviewId)
        }
    }

    override suspend fun deleteReviewAdmin(reviewId: String): Result<AdminDeleteReviewResponse> {
        return runCatching {
            reviewApiService.deleteReviewAdmin(reviewId)
        }
    }

    override suspend fun getReviewCountByVendor(vendorId: String): Result<GetReviewCountByVendorResponse> {
        return runCatching {
            reviewApiService.getReviewCountByVendor(vendorId)
        }
    }

    override suspend fun getMyReviewCount(): Result<GetReviewCountOfCurrentUserResponse> {
        return runCatching {
            reviewApiService.getMyReviewCount()
        }
    }

    override suspend fun hasUserReviewedOrder(orderId: String): Result<CheckIfUserHasReviewedAnOrderResponse> {
        return runCatching {
            reviewApiService.hasUserReviewedOrder(orderId)
        }
    }
}