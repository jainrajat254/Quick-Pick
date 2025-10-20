package org.rajat.quickpick.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.rajat.quickpick.domain.modal.admin.AdminCreateRequest
import org.rajat.quickpick.domain.modal.admin.AdminCreateResponse
import org.rajat.quickpick.domain.modal.admin.AdminLoginRequest
import org.rajat.quickpick.domain.modal.admin.AdminLoginResponse
import org.rajat.quickpick.domain.repository.AdminRepository
import org.rajat.quickpick.utils.UiState

class AdminViewModel(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _adminLoginState = MutableStateFlow<UiState<AdminLoginResponse>>(UiState.Empty)
    val adminLoginState: StateFlow<UiState<AdminLoginResponse>> = _adminLoginState

    private val _adminCreateState = MutableStateFlow<UiState<AdminCreateResponse>>(UiState.Empty)
    val adminCreateState: StateFlow<UiState<AdminCreateResponse>> = _adminCreateState

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

    fun adminLogin(request: AdminLoginRequest) {
        executeWithUiState(_adminLoginState) {
            adminRepository.adminLogin(request)
        }
    }

    fun adminCreate(request: AdminCreateRequest) {
        executeWithUiState(_adminCreateState) {
            adminRepository.adminCreate(request)
        }
    }

    fun resetAdminStates() {
        _adminLoginState.value = UiState.Empty
        _adminCreateState.value = UiState.Empty
    }
}
