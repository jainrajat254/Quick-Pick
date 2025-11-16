package org.rajat.quickpick.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.rajat.quickpick.domain.modal.review.GetVendorRatingStatsResponse
import org.rajat.quickpick.domain.modal.review.getPaginatedReviewsforVendor.GetPaginatedReviewsForVendorResponse
import org.rajat.quickpick.domain.repository.ReviewRepository
import org.rajat.quickpick.utils.UiState

class ReviewViewModel(private val reviewRepository: ReviewRepository): ViewModel() {

    // Single vendor rating state (for direct screen usage)
    private val _vendorRatingState = MutableStateFlow<UiState<GetVendorRatingStatsResponse>>(UiState.Empty)
    val vendorRatingState: StateFlow<UiState<GetVendorRatingStatsResponse>> = _vendorRatingState.asStateFlow()

    // Cache of rating states per vendor for lists
    private val ratingStates = mutableMapOf<String, MutableStateFlow<UiState<GetVendorRatingStatsResponse>>>()

    // Paginated reviews by vendor
    private val _vendorReviewsState = MutableStateFlow<UiState<GetPaginatedReviewsForVendorResponse>>(UiState.Empty)
    val vendorReviewsState: StateFlow<UiState<GetPaginatedReviewsForVendorResponse>> = _vendorReviewsState.asStateFlow()

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
        execute(_vendorRatingState) { reviewRepository.getVendorRating(vendorId) }
    }

    fun getVendorRatingState(vendorId: String): StateFlow<UiState<GetVendorRatingStatsResponse>> {
        val flow = ratingStates.getOrPut(vendorId) { MutableStateFlow(UiState.Empty) }
        if (flow.value is UiState.Empty) {
            execute(flow) { reviewRepository.getVendorRating(vendorId) }
        }
        return flow.asStateFlow()
    }

    fun refreshVendorRating(vendorId: String) {
        val flow = ratingStates.getOrPut(vendorId) { MutableStateFlow(UiState.Empty) }
        execute(flow) { reviewRepository.getVendorRating(vendorId) }
        if (_vendorRatingState.value is UiState.Success && (_vendorRatingState.value as UiState.Success<GetVendorRatingStatsResponse>).data.vendorId == vendorId) {
            execute(_vendorRatingState) { reviewRepository.getVendorRating(vendorId) }
        }
    }

    fun getVendorReviewsPaginated(vendorId: String, page: Int = 0, size: Int = 25) {
        execute(_vendorReviewsState) { reviewRepository.getReviewsByVendorPaginated(vendorId, page, size) }
    }

    fun resetVendorRatingState() { _vendorRatingState.value = UiState.Empty }
    fun resetVendorReviewsState() { _vendorReviewsState.value = UiState.Empty }
}

