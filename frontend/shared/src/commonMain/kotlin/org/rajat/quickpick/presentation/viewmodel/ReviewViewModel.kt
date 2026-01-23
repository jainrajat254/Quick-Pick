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
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private val reviewLogger = Logger.withTag("ReviewViewModel")

@OptIn(ExperimentalTime::class)
class ReviewViewModel(private val reviewRepository: ReviewRepository): ViewModel() {

    private val _vendorRatingState = MutableStateFlow<UiState<GetVendorRatingStatsResponse>>(UiState.Empty)
    val vendorRatingState: StateFlow<UiState<GetVendorRatingStatsResponse>> = _vendorRatingState.asStateFlow()

    private val ratingStates = mutableMapOf<String, MutableStateFlow<UiState<GetVendorRatingStatsResponse>>>()
    private val ratingCacheTimes = mutableMapOf<String, Long>()
    private val cacheValidityDuration = 60_000L

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

    init {
        reviewLogger.d { "ReviewViewModel instance created: ${this.hashCode()}" }
    }

    private fun isCacheValid(vendorId: String): Boolean {
        val cacheTime = ratingCacheTimes[vendorId] ?: return false
        return (Clock.System.now().toEpochMilliseconds() - cacheTime) < cacheValidityDuration
    }

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

    private val ratingLoadingStates = mutableMapOf<String, Boolean>()

    fun getVendorRating(vendorId: String, forceRefresh: Boolean = false) {
        val isLoading = ratingLoadingStates[vendorId] ?: false
        reviewLogger.d { "getVendorRating called for vendor=$vendorId, forceRefresh=$forceRefresh, isLoading=$isLoading" }

        if (isLoading && !forceRefresh) {
            reviewLogger.d { "⏳ REQUEST IN FLIGHT - Skipping duplicate call for vendor=$vendorId" }
            return
        }

        val flow = ratingStates.getOrPut(vendorId) { MutableStateFlow(UiState.Empty) }

        if (!forceRefresh && flow.value is UiState.Success && isCacheValid(vendorId)) {
            reviewLogger.d { "✅ CACHE HIT - Rating already cached for vendor=$vendorId" }
            return
        }

        reviewLogger.d { "❌ CACHE MISS - Fetching rating for vendor=$vendorId" }
        ratingLoadingStates[vendorId] = true
        viewModelScope.launch {
            flow.value = UiState.Loading
            val result = reviewRepository.getVendorRating(vendorId)
            result.fold(
                onSuccess = { data ->
                    flow.value = UiState.Success(data)
                    ratingCacheTimes[vendorId] = Clock.System.now().toEpochMilliseconds()
                    reviewLogger.d { "Rating cached for vendor=$vendorId" }
                },
                onFailure = { error ->
                    flow.value = UiState.Error(error.message ?: "Unknown error")
                    reviewLogger.e { "Rating fetch failed for vendor=$vendorId: ${error.message}" }
                }
            )
            ratingLoadingStates[vendorId] = false
        }
    }

    fun getVendorRatingState(vendorId: String): StateFlow<UiState<GetVendorRatingStatsResponse>> {
        reviewLogger.d { "getVendorRatingState requested for vendor=$vendorId" }
        val flow = ratingStates.getOrPut(vendorId) { MutableStateFlow(UiState.Empty) }
        if (flow.value is UiState.Success) {
            reviewLogger.d { "✅ Returning cached rating state for vendor=$vendorId" }
        }
        return flow.asStateFlow()
    }

    fun ensureVendorRatingLoaded(vendorId: String) {
        val flow = ratingStates.getOrPut(vendorId) { MutableStateFlow(UiState.Empty) }
        val isLoading = ratingLoadingStates[vendorId] ?: false
        if (isLoading) {
            reviewLogger.d { "ensureVendorRatingLoaded: Already loading for vendor=$vendorId, skipping" }
            return
        }
        if (flow.value is UiState.Empty || (flow.value !is UiState.Loading && flow.value !is UiState.Success) || (flow.value is UiState.Success && !isCacheValid(vendorId))) {
            reviewLogger.d { "ensureVendorRatingLoaded: State is Empty/Error or cache invalid, triggering fetch for vendor=$vendorId" }
            getVendorRating(vendorId, forceRefresh = false)
        } else {
            reviewLogger.d { "ensureVendorRatingLoaded: Data already available for vendor=$vendorId" }
        }
    }

    fun refreshVendorRating(vendorId: String) {
        reviewLogger.d { "refreshVendorRating for vendor=$vendorId (force refresh)" }
        ratingCacheTimes.remove(vendorId)
        getVendorRating(vendorId, forceRefresh = true)
    }

    private val cachedVendorReviews = mutableMapOf<String, GetPaginatedReviewsForVendorResponse>()
    private val vendorReviewsCacheTime = mutableMapOf<String, Long>()
    private val vendorReviewsLoadingStates = mutableMapOf<String, Boolean>()
    private var currentVendorReviewsId: String? = null

    fun getVendorReviewsPaginated(vendorId: String, page: Int = 0, size: Int = 25, forceRefresh: Boolean = false) {
        val isLoading = vendorReviewsLoadingStates[vendorId] ?: false
        reviewLogger.d { "getVendorReviewsPaginated vendor=$vendorId page=$page size=$size forceRefresh=$forceRefresh isLoading=$isLoading" }

        if (isLoading && !forceRefresh) {
            reviewLogger.d { "⏳ REQUEST IN FLIGHT - Skipping duplicate call for vendor=$vendorId" }
            return
        }

        val cacheTime = vendorReviewsCacheTime[vendorId] ?: 0
        if (!forceRefresh && cachedVendorReviews.containsKey(vendorId) &&
            (Clock.System.now().toEpochMilliseconds() - cacheTime) < cacheValidityDuration) {
            if (_vendorReviewsState.value is UiState.Success && currentVendorReviewsId == vendorId) {
                reviewLogger.d { "✅ CACHE HIT - Reviews already cached for vendor=$vendorId" }
                return
            }
            currentVendorReviewsId = vendorId
            _vendorReviewsState.value = UiState.Success(cachedVendorReviews[vendorId]!!)
            return
        }

        reviewLogger.d { "❌ CACHE MISS - Fetching reviews for vendor=$vendorId" }
        vendorReviewsLoadingStates[vendorId] = true
        viewModelScope.launch {
            _vendorReviewsState.value = UiState.Loading
            val result = reviewRepository.getReviewsByVendorPaginated(vendorId, page, size)
            result.fold(
                onSuccess = { data ->
                    cachedVendorReviews[vendorId] = data
                    vendorReviewsCacheTime[vendorId] = Clock.System.now().toEpochMilliseconds()
                    currentVendorReviewsId = vendorId
                    _vendorReviewsState.value = UiState.Success(data)
                    reviewLogger.d { "Reviews cached for vendor=$vendorId" }
                },
                onFailure = { error ->
                    _vendorReviewsState.value = UiState.Error(error.message ?: "Unknown error")
                    reviewLogger.e { "Reviews fetch failed for vendor=$vendorId: ${error.message}" }
                }
            )
            vendorReviewsLoadingStates[vendorId] = false
        }
    }

    fun isVendorReviewsDataLoadedFor(vendorId: String): Boolean {
        return _vendorReviewsState.value is UiState.Success && currentVendorReviewsId == vendorId
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
