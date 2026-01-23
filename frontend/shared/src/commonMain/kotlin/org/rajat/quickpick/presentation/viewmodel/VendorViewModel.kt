package org.rajat.quickpick.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.rajat.quickpick.domain.modal.search.GetAllVendorsInCollegeResponse
import org.rajat.quickpick.domain.modal.search.GetVendorByIDResponse
import org.rajat.quickpick.domain.modal.search.SearchVendorsResponse
import org.rajat.quickpick.domain.repository.SearchRepository
import org.rajat.quickpick.utils.UiState
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import co.touchlab.kermit.Logger

@OptIn(ExperimentalTime::class)
class VendorViewModel(
    private val searchRepository: SearchRepository
) : ViewModel(){

    private val vendorLogger = Logger.withTag("VendorViewModel_Cache")

    private val _vendorDetailState =
        MutableStateFlow<UiState<GetVendorByIDResponse>>(UiState.Empty)
    val vendorsDetailState : StateFlow<UiState<GetVendorByIDResponse>> = _vendorDetailState

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _currentVendorId = MutableStateFlow<String?>(null)
    val currentVendorId: StateFlow<String?> = _currentVendorId

    private val cachedVendorDetails: MutableMap<String, GetVendorByIDResponse> = mutableMapOf()
    private val vendorDetailsCacheTime: MutableMap<String, Long> = mutableMapOf()
    private val vendorLoadingStates: MutableMap<String, Boolean> = mutableMapOf()
    private val cacheValidityDuration = 60_000L

    private fun isCacheValid(cacheTime: Long): Boolean {
        return (Clock.System.now().toEpochMilliseconds() - cacheTime) < cacheValidityDuration
    }

    private fun <T> executeWithUiState(
        stateFlow: MutableStateFlow<UiState<T>>,
        block: suspend () -> Result<T>
    ) {
        viewModelScope.launch {
            stateFlow.value = UiState.Loading
            val result = block()
            stateFlow.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun getVendorsDetails(vendorId: String, forceRefresh: Boolean = false) {
        val isLoading = vendorLoadingStates[vendorId] ?: false
        vendorLogger.d { "getVendorsDetails called for vendor=$vendorId, forceRefresh=$forceRefresh, isLoading=$isLoading" }

        if (isLoading && !forceRefresh) {
            vendorLogger.d { "⏳ REQUEST IN FLIGHT - Skipping duplicate call for vendor=$vendorId" }
            return
        }

        val cachedTime = vendorDetailsCacheTime[vendorId] ?: 0
        if (!forceRefresh && cachedVendorDetails.containsKey(vendorId) && isCacheValid(cachedTime)) {
            if (_vendorDetailState.value is UiState.Success && _currentVendorId.value == vendorId) {
                vendorLogger.d { "✅ CACHE HIT - State already Success for vendor=$vendorId" }
                return
            }
            vendorLogger.d { "✅ CACHE HIT - Setting state to cached Success for vendor=$vendorId" }
            _currentVendorId.value = vendorId
            _vendorDetailState.value = UiState.Success(cachedVendorDetails[vendorId]!!)
            return
        }

        vendorLogger.d { "❌ CACHE MISS - Making API call for vendor=$vendorId" }
        vendorLoadingStates[vendorId] = true
        viewModelScope.launch {
            if (forceRefresh) {
                _isRefreshing.value = true
            } else {
                _vendorDetailState.value = UiState.Loading
            }

            val result = searchRepository.getVendorById(vendorId)
            result.fold(
                onSuccess = { data ->
                    cachedVendorDetails[vendorId] = data
                    vendorDetailsCacheTime[vendorId] = Clock.System.now().toEpochMilliseconds()
                    _currentVendorId.value = vendorId
                    _vendorDetailState.value = UiState.Success(data)
                    vendorLogger.d { "API call successful, data cached for vendor=$vendorId" }
                },
                onFailure = { error ->
                    _vendorDetailState.value = UiState.Error(error.message ?: "Unknown error")
                    vendorLogger.e { "API call failed for vendor=$vendorId: ${error.message}" }
                }
            )
            _isRefreshing.value = false
            vendorLoadingStates[vendorId] = false
        }
    }

    fun isDataLoadedFor(vendorId: String): Boolean {
        return _vendorDetailState.value is UiState.Success && _currentVendorId.value == vendorId
    }

    fun invalidateVendorCache(vendorId: String) {
        cachedVendorDetails.remove(vendorId)
        vendorDetailsCacheTime.remove(vendorId)
    }

    fun invalidateAllCache() {
        cachedVendorDetails.clear()
        vendorDetailsCacheTime.clear()
    }

    fun resetVendorDetailState() {
        _vendorDetailState.value = UiState.Empty
        _currentVendorId.value = null
    }
}