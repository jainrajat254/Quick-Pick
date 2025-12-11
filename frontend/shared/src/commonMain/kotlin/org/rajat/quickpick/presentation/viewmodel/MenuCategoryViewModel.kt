package org.rajat.quickpick.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.rajat.quickpick.domain.modal.vendorMenuCategories.GetDefaultVendorCategoriesResponse
import org.rajat.quickpick.domain.modal.vendorMenuCategories.ResetVendorCategoriesToDefaultResponse
import org.rajat.quickpick.domain.modal.vendorMenuCategories.UpdateVendorCategoriesRequest
import org.rajat.quickpick.domain.modal.vendorMenuCategories.UpdateVendorCategoriesResponse
import org.rajat.quickpick.domain.repository.MenuCategoryRepository
import org.rajat.quickpick.utils.UiState

class MenuCategoryViewModel(
    private val menuCategoryRepository: MenuCategoryRepository
) : ViewModel() {

    private val _getDefaultCategoriesState =
        MutableStateFlow<UiState<GetDefaultVendorCategoriesResponse>>(UiState.Empty)
    val getDefaultCategoriesState: StateFlow<UiState<GetDefaultVendorCategoriesResponse>> =
        _getDefaultCategoriesState

    private val _resetCategoriesState =
        MutableStateFlow<UiState<ResetVendorCategoriesToDefaultResponse>>(UiState.Empty)
    val resetCategoriesState: StateFlow<UiState<ResetVendorCategoriesToDefaultResponse>> =
        _resetCategoriesState

    private val _updateCategoriesState =
        MutableStateFlow<UiState<UpdateVendorCategoriesResponse>>(UiState.Empty)
    val updateCategoriesState: StateFlow<UiState<UpdateVendorCategoriesResponse>> =
        _updateCategoriesState

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

    fun getDefaultVendorCategories() {
        executeWithUiState(_getDefaultCategoriesState) {
            menuCategoryRepository.getDefaultVendorCategories()
        }
    }

    fun resetVendorCategoriesToDefault() {
        executeWithUiState(_resetCategoriesState) {
            menuCategoryRepository.resetVendorCategoriesToDefault()
        }
    }

    fun updateVendorCategories(request: UpdateVendorCategoriesRequest) {
        executeWithUiState(_updateCategoriesState) {
            menuCategoryRepository.updateVendorCategories(request)
        }
    }

    fun resetMenuCategoryStates() {
        _getDefaultCategoriesState.value = UiState.Empty
        _resetCategoriesState.value = UiState.Empty
        _updateCategoriesState.value = UiState.Empty
    }
}
