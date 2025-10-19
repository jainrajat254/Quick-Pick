package org.rajat.quickpick.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.rajat.quickpick.domain.modal.auth.LoginUserRequest
import org.rajat.quickpick.domain.modal.auth.LoginUserResponse
import org.rajat.quickpick.domain.modal.auth.LoginVendorRequest
import org.rajat.quickpick.domain.modal.auth.LoginVendorResponse
import org.rajat.quickpick.domain.modal.auth.RegisterUserRequest
import org.rajat.quickpick.domain.modal.auth.RegisterUserResponse
import org.rajat.quickpick.domain.modal.auth.RegisterVendorRequest
import org.rajat.quickpick.domain.modal.auth.RegisterVendorResponse
import org.rajat.quickpick.domain.repository.AuthRepository
import org.rajat.quickpick.utils.UiState

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _userLoginState = MutableStateFlow<UiState<LoginUserResponse>>(UiState.Empty)
    val userLoginState: StateFlow<UiState<LoginUserResponse>> = _userLoginState

    private val _vendorLoginState = MutableStateFlow<UiState<LoginVendorResponse>>(UiState.Empty)
    val vendorLoginState: StateFlow<UiState<LoginVendorResponse>> = _vendorLoginState

    private val _userRegisterState = MutableStateFlow<UiState<RegisterUserResponse>>(UiState.Empty)
    val userRegisterState: StateFlow<UiState<RegisterUserResponse>> = _userRegisterState

    private val _vendorRegisterState =
        MutableStateFlow<UiState<RegisterVendorResponse>>(UiState.Empty)
    val vendorRegisterState: StateFlow<UiState<RegisterVendorResponse>> = _vendorRegisterState

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

    fun loginUser(request: LoginUserRequest) {
        executeWithUiState(_userLoginState) {
            authRepository.userLogin(request)
        }
    }

    fun loginVendor(request: LoginVendorRequest) {
        executeWithUiState(_vendorLoginState) {
            authRepository.vendorLogin(request)
        }
    }

    fun registerUser(request: RegisterUserRequest) {
        executeWithUiState(_userRegisterState) {
            authRepository.userRegister(request)
        }
    }

    fun registerVendor(request: RegisterVendorRequest) {
        executeWithUiState(_vendorRegisterState) {
            authRepository.vendorRegister(request)
        }
    }
}
