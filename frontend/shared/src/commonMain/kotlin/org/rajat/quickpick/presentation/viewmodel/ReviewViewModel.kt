package org.rajat.quickpick.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.rajat.quickpick.domain.modal.review.*
import org.rajat.quickpick.domain.modal.review.getPaginatedReviewsforVendor.GetPaginatedReviewsForVendorResponse
import org.rajat.quickpick.domain.repository.ReviewRepository
import org.rajat.quickpick.utils.UiState

private val reviewLogger = Logger.withTag("ReviewViewModel")

class ReviewViewModel(private val reviewRepository: ReviewRepository): ViewModel() {

    private val _vendorRatingState = MutableStateFlow<UiState<GetVendorRatingStatsResponse>>(UiState.Empty)
    val vendorRatingState: StateFlow<UiState<GetVendorRatingStatsResponse>> = _vendorRatingState.asStateFlow()

    private val ratingStates = mutableMapOf<String, MutableStateFlow<UiState<GetVendorRatingStatsResponse>>>()

    private val _vendorReviewsState = MutableStateFlow<UiState<GetPaginatedReviewsForVendorResponse>>(UiState.Empty)
    val vendorReviewsState: StateFlow<UiState<GetPaginatedReviewsForVendorResponse>> = _vendorReviewsState.asStateFlow()

    private val _createReviewState = MutableStateFlow<UiState<CreateReviewStudentResponse>>(UiState.Empty)
    val createReviewState: StateFlow<UiState<CreateReviewStudentResponse>> = _createReviewState.asStateFlow()

    private val _updateReviewState = MutableStateFlow<UiState<UpdateReviewStudentResponse>>(UiState.Empty)
    val updateReviewState: StateFlow<UiState<UpdateReviewStudentResponse>> = _updateReviewState.asStateFlow()

    private val _deleteReviewState = MutableStateFlow<UiState<DeleteReviewStudentResponse>>(UiState.Empty)
    val deleteReviewState: StateFlow<UiState<DeleteReviewStudentResponse>> = _deleteReviewState.asStateFlow()

    private val _myReviewsState = MutableStateFlow<UiState<GetLoggedInStudentsReviewsResponse>>(UiState.Empty)
    val myReviewsState: StateFlow<UiState<GetLoggedInStudentsReviewsResponse>> = _myReviewsState.asStateFlow()

    private val _hasReviewedState = MutableStateFlow<UiState<CheckIfUserHasReviewedAnOrderResponse>>(UiState.Empty)
    val hasReviewedState: StateFlow<UiState<CheckIfUserHasReviewedAnOrderResponse>> = _hasReviewedState.asStateFlow()

    private fun <T> execute(state: MutableStateFlow<UiState<T>>, block: suspend () -> Result<T>) {
        viewModelScope.launch {
            state.value = UiState.Loading
            val result = block()
            state.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun getVendorRating(vendorId: String) {
        reviewLogger.d { "getVendorRating called for vendor=$vendorId" }
        execute(_vendorRatingState) { reviewRepository.getVendorRating(vendorId) }
    }

    fun getVendorRatingState(vendorId: String): StateFlow<UiState<GetVendorRatingStatsResponse>> {
        reviewLogger.d { "getVendorRatingState requested for vendor=$vendorId" }
        val flow = ratingStates.getOrPut(vendorId) { MutableStateFlow(UiState.Empty) }
        if (flow.value is UiState.Empty) {
            reviewLogger.d { "Initial fetch for vendor rating for vendor=$vendorId" }
            execute(flow) { reviewRepository.getVendorRating(vendorId) }
        }
        return flow.asStateFlow()
    }

    fun refreshVendorRating(vendorId: String) {
        reviewLogger.d { "refreshVendorRating for vendor=$vendorId" }
        val flow = ratingStates.getOrPut(vendorId) { MutableStateFlow(UiState.Empty) }
        execute(flow) { reviewRepository.getVendorRating(vendorId) }
        if (_vendorRatingState.value is UiState.Success && (_vendorRatingState.value as UiState.Success<GetVendorRatingStatsResponse>).data.vendorId == vendorId) {
            reviewLogger.d { "refreshing single vendorRatingState for vendor=$vendorId" }
            execute(_vendorRatingState) { reviewRepository.getVendorRating(vendorId) }
        }
    }

    fun getVendorReviewsPaginated(vendorId: String, page: Int = 0, size: Int = 25) {
        reviewLogger.d { "getVendorReviewsPaginated vendor=$vendorId page=$page size=$size" }
        execute(_vendorReviewsState) { reviewRepository.getReviewsByVendorPaginated(vendorId, page, size) }
    }

    // New methods to connect UI create/update/delete and user-specific endpoints
    fun createReview(createRequest: CreateReviewStudentRequest) {
        reviewLogger.d { "createReview for order=${createRequest.orderId} vendor=${createRequest.vendorId} rating=${createRequest.rating}" }
        execute(_createReviewState) { reviewRepository.createReview(createRequest) }
    }

    fun updateReview(reviewId: String, updateRequest: CreateReviewStudentRequest) {
        reviewLogger.d { "updateReview id=$reviewId rating=${updateRequest.rating}" }
        execute(_updateReviewState) { reviewRepository.updateReview(reviewId, updateRequest) }
    }

    fun deleteReview(reviewId: String) {
        reviewLogger.d { "deleteReview id=$reviewId" }
        execute(_deleteReviewState) { reviewRepository.deleteReview(reviewId) }
    }

    fun getMyReviews() {
        reviewLogger.d { "getMyReviews called" }
        execute(_myReviewsState) { reviewRepository.getMyReviews() }
    }

    fun hasUserReviewedOrder(orderId: String) {
        reviewLogger.d { "hasUserReviewedOrder called for order=$orderId" }
        execute(_hasReviewedState) { reviewRepository.hasUserReviewedOrder(orderId) }
    }

    // Reset helpers so UI can clear transient states after handling
    fun resetCreateReviewState() { _createReviewState.value = UiState.Empty }
    fun resetUpdateReviewState() { _updateReviewState.value = UiState.Empty }
    fun resetDeleteReviewState() { _deleteReviewState.value = UiState.Empty }
    fun resetHasReviewedState() { _hasReviewedState.value = UiState.Empty }
    fun resetMyReviewsState() { _myReviewsState.value = UiState.Empty }
    fun resetVendorReviewsState() { _vendorReviewsState.value = UiState.Empty }
    fun resetVendorRatingState() { _vendorRatingState.value = UiState.Empty }
}
