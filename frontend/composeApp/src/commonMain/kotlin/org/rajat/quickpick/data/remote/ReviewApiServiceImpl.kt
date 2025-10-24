package org.rajat.quickpick.data.remote


import io.ktor.client.HttpClient
import org.rajat.quickpick.domain.modal.review.*
import org.rajat.quickpick.domain.modal.review.getPaginatedReviewsforVendor.GetPaginatedReviewsForVendorResponse
import org.rajat.quickpick.domain.service.ReviewApiService
import org.rajat.quickpick.utils.Constants
import org.rajat.quickpick.utils.network.*

class ReviewApiServiceImpl(private val httpClient: HttpClient) : ReviewApiService {

    override suspend fun createReview(createReviewStudentRequest: CreateReviewStudentRequest): CreateReviewStudentResponse {
        return httpClient.safePost(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.CREATE_REVIEW}",
            body = createReviewStudentRequest
        )
    }

    override suspend fun getReviewById(reviewId: String): GetReviewByIDResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_REVIEW_BY_ID}$reviewId"
        )
    }

    override suspend fun getReviewsByVendor(vendorId: String): GetAllReviewsForAVendorResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_REVIEWS_BY_VENDOR}$vendorId"
        )
    }

    override suspend fun getReviewsByVendorPaginated(vendorId: String, page: Int, size: Int): GetPaginatedReviewsForVendorResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_REVIEWS_BY_VENDOR_PAGINATED}$vendorId/paginated",
            queryParams = mapOf("page" to page.toString(), "size" to size.toString())
        )
    }

    override suspend fun getMyReviews(): GetLoggedInStudentsReviewsResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_REVIEWS_BY_USER}"
        )
    }

    override suspend fun getReviewsByRating(vendorId: String, rating: Int): GetReviewsByVendorSpecificRatingResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_REVIEWS_BY_RATING}$vendorId/rating/$rating"
        )
    }

    override suspend fun getReviewsByRatingRange(vendorId: String, minRating: Int): GetReviewsByVendorSpecificRatingResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_REVIEWS_BY_RATING_ABOVE}$vendorId/rating-above/$minRating"
        )
    }

    override suspend fun getVendorRating(vendorId: String): GetVendorRatingStatsResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_VENDOR_RATING}$vendorId/statistics"
        )
    }

    override suspend fun updateReview(reviewId: String, createReviewStudentRequest: CreateReviewStudentRequest): UpdateReviewStudentResponse {
        return httpClient.safePut(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.UPDATE_REVIEW}$reviewId",
            body = createReviewStudentRequest
        )
    }

    override suspend fun deleteReview(reviewId: String): DeleteReviewStudentResponse {
        return httpClient.safeDelete(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.DELETE_REVIEW}$reviewId"
        )
    }

    override suspend fun deleteReviewAdmin(reviewId: String): AdminDeleteReviewResponse {
        return httpClient.safeDelete(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.DELETE_REVIEW_ADMIN}$reviewId"
        )
    }

    override suspend fun getReviewCountByVendor(vendorId: String): GetReviewCountByVendorResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_REVIEW_COUNT_BY_VENDOR}$vendorId/count"
        )
    }

    override suspend fun getMyReviewCount(): GetReviewCountOfCurrentUserResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_MY_REVIEW_COUNT}"
        )
    }

    override suspend fun hasUserReviewedOrder(orderId: String): CheckIfUserHasReviewedAnOrderResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.HAS_USER_REVIEWED_ORDER}$orderId/exists"
        )
    }
}