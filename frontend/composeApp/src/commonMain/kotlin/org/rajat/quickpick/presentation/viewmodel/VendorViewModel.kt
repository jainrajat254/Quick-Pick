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

class VendorViewModel(
    private val searchRepository: SearchRepository
) : ViewModel(){

    private val _vendorDetailState =
        MutableStateFlow<UiState<GetVendorByIDResponse>>(UiState.Empty)
    val vendorsDetailState : StateFlow<UiState<GetVendorByIDResponse>> = _vendorDetailState



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
    fun getVendorsDetails(vendorId : String) {
        executeWithUiState(_vendorDetailState) {
            searchRepository.getVendorById(vendorId)
        }
    }

    fun resetVendorDetailState() {
        _vendorDetailState.value = UiState.Empty
    }
}