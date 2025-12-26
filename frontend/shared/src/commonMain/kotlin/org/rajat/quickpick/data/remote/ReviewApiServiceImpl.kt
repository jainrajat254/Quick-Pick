package org.rajat.quickpick.data.remote


import io.ktor.client.HttpClient
import co.touchlab.kermit.Logger
import org.rajat.quickpick.domain.modal.review.*
import org.rajat.quickpick.domain.modal.review.getPaginatedReviewsforVendor.GetPaginatedReviewsForVendorResponse
import org.rajat.quickpick.domain.service.ReviewApiService
import org.rajat.quickpick.utils.Constants
import org.rajat.quickpick.utils.network.*

private val reviewLogger = Logger.withTag("ReviewApiService")

class ReviewApiServiceImpl(private val httpClient: HttpClient) : ReviewApiService {

    override suspend fun createReview(createReviewStudentRequest: CreateReviewStudentRequest): CreateReviewStudentResponse {
        reviewLogger.d { "POST create review -> ${Constants.BASE_URL}${Constants.Endpoints.CREATE_REVIEW}" }
        return httpClient.safePost(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.CREATE_REVIEW}",
            body = createReviewStudentRequest
        )
    }

    override suspend fun getReviewById(reviewId: String): GetReviewByIDResponse {
        val endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_REVIEW_BY_ID}$reviewId"
        reviewLogger.d { "GET review by id -> $endpoint" }
        return httpClient.safeGet(
            endpoint = endpoint
        )
    }

    override suspend fun getReviewsByVendor(vendorId: String): GetAllReviewsForAVendorResponse {
        val endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_REVIEWS_BY_VENDOR}$vendorId"
        reviewLogger.d { "GET reviews by vendor -> $endpoint" }
        return try {
            httpClient.safeGet(
                endpoint = endpoint
            )
        } catch (e: Exception) {
            reviewLogger.e(e) { "Failed to fetch reviews for vendor=$vendorId: ${e.message}" }
            throw e
        }
    }

    override suspend fun getReviewsByVendorPaginated(vendorId: String, page: Int, size: Int): GetPaginatedReviewsForVendorResponse {
        val endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_REVIEWS_BY_VENDOR_PAGINATED}$vendorId/paginated"
        reviewLogger.d { "GET reviews paginated -> $endpoint?page=$page&size=$size" }
        return try {
            httpClient.safeGet(
                endpoint = endpoint,
                queryParams = mapOf("page" to page.toString(), "size" to size.toString())
            )
        } catch (e: Exception) {
            reviewLogger.e(e) { "Failed to fetch paginated reviews for vendor=$vendorId: ${e.message}" }
            throw e
        }
    }

    override suspend fun getMyReviews(): GetLoggedInStudentsReviewsResponse {
        val endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_REVIEWS_BY_USER}"
        reviewLogger.d { "GET my reviews -> $endpoint" }
        return httpClient.safeGet(
            endpoint = endpoint
        )
    }

    override suspend fun getReviewsByRating(vendorId: String, rating: Int): GetReviewsByVendorSpecificRatingResponse {
        val endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_REVIEWS_BY_RATING}$vendorId/rating/$rating"
        reviewLogger.d { "GET reviews by rating -> $endpoint" }
        return httpClient.safeGet(
            endpoint = endpoint
        )
    }

    override suspend fun getReviewsByRatingRange(vendorId: String, minRating: Int): GetReviewsByVendorSpecificRatingResponse {
        val endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_REVIEWS_BY_RATING_ABOVE}$vendorId/rating-above/$minRating"
        reviewLogger.d { "GET reviews by rating above -> $endpoint" }
        return httpClient.safeGet(
            endpoint = endpoint
        )
    }

    override suspend fun getVendorRating(vendorId: String): GetVendorRatingStatsResponse {
        val endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_VENDOR_RATING}$vendorId/statistics"
        reviewLogger.d { "GET vendor rating -> $endpoint" }
        return try {
            val resp = httpClient.safeGet<GetVendorRatingStatsResponse>(
                endpoint = endpoint
            )
            reviewLogger.d { "Vendor rating fetched: vendor=$vendorId avg=${resp.averageRating} total=${resp.totalReviews}" }
            resp
        } catch (e: Exception) {
            reviewLogger.e(e) { "Failed to fetch vendor rating for vendor=$vendorId: ${e.message}" }
            throw e
        }
    }

    override suspend fun updateReview(reviewId: String, createReviewStudentRequest: CreateReviewStudentRequest): UpdateReviewStudentResponse {
        val endpoint = "${Constants.BASE_URL}${Constants.Endpoints.UPDATE_REVIEW}$reviewId"
        reviewLogger.d { "PUT update review -> $endpoint" }
        return httpClient.safePut(
            endpoint = endpoint,
            body = createReviewStudentRequest
        )
    }

    override suspend fun deleteReview(reviewId: String): DeleteReviewStudentResponse {
        val endpoint = "${Constants.BASE_URL}${Constants.Endpoints.DELETE_REVIEW}$reviewId"
        reviewLogger.d { "DELETE review -> $endpoint" }
        return httpClient.safeDelete(
            endpoint = endpoint
        )
    }

    override suspend fun deleteReviewAdmin(reviewId: String): AdminDeleteReviewResponse {
        val endpoint = "${Constants.BASE_URL}${Constants.Endpoints.DELETE_REVIEW_ADMIN}$reviewId"
        reviewLogger.d { "DELETE review (admin) -> $endpoint" }
        return httpClient.safeDelete(
            endpoint = endpoint
        )
    }

    override suspend fun getReviewCountByVendor(vendorId: String): GetReviewCountByVendorResponse {
        val endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_REVIEW_COUNT_BY_VENDOR}$vendorId/count"
        reviewLogger.d { "GET review count -> $endpoint" }
        return httpClient.safeGet(
            endpoint = endpoint
        )
    }

    override suspend fun getMyReviewCount(): GetReviewCountOfCurrentUserResponse {
        val endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_MY_REVIEW_COUNT}"
        reviewLogger.d { "GET my review count -> $endpoint" }
        return httpClient.safeGet(
            endpoint = endpoint
        )
    }

    override suspend fun hasUserReviewedOrder(orderId: String): CheckIfUserHasReviewedAnOrderResponse {
        val endpoint = "${Constants.BASE_URL}${Constants.Endpoints.HAS_USER_REVIEWED_ORDER}$orderId/exists"
        reviewLogger.d { "GET has user reviewed order -> $endpoint" }
        return httpClient.safeGet(
            endpoint = endpoint
        )
    }
}