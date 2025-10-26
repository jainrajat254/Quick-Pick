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

class HomeViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _vendorsInCollegeState =
        MutableStateFlow<UiState<GetAllVendorsInCollegeResponse>>(UiState.Empty)
    val vendorsInCollegeState : StateFlow<UiState<GetAllVendorsInCollegeResponse>> = _vendorsInCollegeState

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

    fun getVendorsInCollege() {
        executeWithUiState(_vendorsInCollegeState) {
            searchRepository.getAllVendorsInCollege()
        }
    }

    fun resetVendorsInCollegeState() {
        _vendorsInCollegeState.value = UiState.Empty
    }

}