package org.rajat.quickpick.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.rajat.quickpick.domain.modal.adminManagement.getAllVendors.GetAllVendorsResponse
import org.rajat.quickpick.domain.modal.search.GetAllVendorsInCollegeResponse
import org.rajat.quickpick.domain.repository.SearchRepository
import org.rajat.quickpick.utils.UiState
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class HomeViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val logger = Logger.withTag("HomeViewModel_Cache")

    private val _vendorsInCollegeState =
        MutableStateFlow<UiState<GetAllVendorsInCollegeResponse>>(UiState.Empty)
    val vendorsInCollegeState : StateFlow<UiState<GetAllVendorsInCollegeResponse>> = _vendorsInCollegeState

    private val _selectedVendorId = MutableStateFlow<String?>(null)
    val selectedVendorId: StateFlow<String?> = _selectedVendorId

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private var cachedVendorsInCollege: GetAllVendorsInCollegeResponse? = null
    private var vendorsCacheTime: Long = 0
    private val cacheValidityDuration = 60_000L

    init {
        logger.d { "HomeViewModel instance created: ${this.hashCode()}" }
    }

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

    private var isVendorsLoading = false

    fun getVendorsInCollege(forceRefresh: Boolean = false) {
        logger.d { "getVendorsInCollege called - forceRefresh=$forceRefresh, cachedData=${cachedVendorsInCollege != null}, cacheValid=${if (cachedVendorsInCollege != null) isCacheValid(vendorsCacheTime) else false}, currentState=${_vendorsInCollegeState.value::class.simpleName}, isLoading=$isVendorsLoading" }

        if (isVendorsLoading && !forceRefresh) {
            logger.d { "⏳ REQUEST IN FLIGHT - Skipping duplicate call" }
            return
        }

        if (!forceRefresh && _vendorsInCollegeState.value is UiState.Success && cachedVendorsInCollege != null && isCacheValid(vendorsCacheTime)) {
            logger.d { "✅ CACHE HIT - State already Success, returning without API call" }
            return
        }

        if (!forceRefresh && cachedVendorsInCollege != null && isCacheValid(vendorsCacheTime)) {
            logger.d { "✅ CACHE HIT - Setting state to cached Success" }
            _vendorsInCollegeState.value = UiState.Success(cachedVendorsInCollege!!)
            return
        }

        logger.d { "❌ CACHE MISS - Making API call" }
        isVendorsLoading = true
        viewModelScope.launch {
            if (forceRefresh) {
                _isRefreshing.value = true
            } else {
                _vendorsInCollegeState.value = UiState.Loading
            }

            val result = searchRepository.getAllVendorsInCollege()
            result.fold(
                onSuccess = { data ->
                    cachedVendorsInCollege = data
                    vendorsCacheTime = Clock.System.now().toEpochMilliseconds()
                    _vendorsInCollegeState.value = UiState.Success(data)
                    logger.d { "API call successful, data cached" }
                },
                onFailure = { error ->
                    _vendorsInCollegeState.value = UiState.Error(error.message ?: "Unknown error")
                    logger.e { "API call failed: ${error.message}" }
                }
            )
            _isRefreshing.value = false
            isVendorsLoading = false
        }
    }

    fun setSelectedVendorId(vendorId: String?) {
        _selectedVendorId.value = vendorId
    }

    fun invalidateCache() {
        cachedVendorsInCollege = null
        vendorsCacheTime = 0
        logger.d { "Cache invalidated" }
    }

    fun resetVendorsInCollegeState() {
        _vendorsInCollegeState.value = UiState.Empty
        logger.d { "State reset to Empty" }
    }

}