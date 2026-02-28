package org.rajat.quickpick.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _vendorsInCollegeState =
        MutableStateFlow<UiState<GetAllVendorsInCollegeResponse>>(UiState.Empty)
    val vendorsInCollegeState : StateFlow<UiState<GetAllVendorsInCollegeResponse>> = _vendorsInCollegeState

    private val _selectedVendorId = MutableStateFlow<String?>(null)
    val selectedVendorId: StateFlow<String?> = _selectedVendorId

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private var cachedVendorsInCollege: GetAllVendorsInCollegeResponse? = null
    private var vendorsCacheTime: Long = 0
    private val cacheValidityDuration = 300_000L

    init {
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
        if (isVendorsLoading && !forceRefresh) {
            return
        }

        if (!forceRefresh && _vendorsInCollegeState.value is UiState.Success && cachedVendorsInCollege != null && isCacheValid(vendorsCacheTime)) {
            return
        }

        if (!forceRefresh && cachedVendorsInCollege != null && isCacheValid(vendorsCacheTime)) {
            _vendorsInCollegeState.value = UiState.Success(cachedVendorsInCollege!!)
            return
        }

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
                },
                onFailure = { error ->
                    _vendorsInCollegeState.value = UiState.Error(error.message ?: "Unknown error")
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
    }

    fun resetVendorsInCollegeState() {
        _vendorsInCollegeState.value = UiState.Empty
    }

}