package org.rajat.quickpick.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.rajat.quickpick.domain.modal.auth.ForgotPasswordRequest
import org.rajat.quickpick.domain.modal.auth.ForgotPasswordResponse
import org.rajat.quickpick.domain.modal.auth.LoginUserRequest
import org.rajat.quickpick.domain.modal.auth.LoginUserResponse
import org.rajat.quickpick.domain.modal.auth.LoginVendorRequest
import org.rajat.quickpick.domain.modal.auth.LoginVendorResponse
import org.rajat.quickpick.domain.modal.auth.LogoutRequest
import org.rajat.quickpick.domain.modal.auth.LogoutResponse
import org.rajat.quickpick.domain.modal.auth.RefreshTokenRequest
import org.rajat.quickpick.domain.modal.auth.RefreshTokenResponse
import org.rajat.quickpick.domain.modal.auth.RegisterUserRequest
import org.rajat.quickpick.domain.modal.auth.RegisterUserResponse
import org.rajat.quickpick.domain.modal.auth.RegisterVendorRequest
import org.rajat.quickpick.domain.modal.auth.RegisterVendorResponse
import org.rajat.quickpick.domain.modal.auth.ResetPasswordRequest
import org.rajat.quickpick.domain.modal.auth.ResetPasswordResponse
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

    private val _refreshTokenState = MutableStateFlow<UiState<RefreshTokenResponse>>(UiState.Empty)
    val refreshTokenState: StateFlow<UiState<RefreshTokenResponse>> = _refreshTokenState

    private val _forgotPasswordState =
        MutableStateFlow<UiState<ForgotPasswordResponse>>(UiState.Empty)
    val forgotPasswordState: StateFlow<UiState<ForgotPasswordResponse>> = _forgotPasswordState

    private val _resetPasswordState =
        MutableStateFlow<UiState<ResetPasswordResponse>>(UiState.Empty)
    val resetPasswordState: StateFlow<UiState<ResetPasswordResponse>> = _resetPasswordState

    private val _logoutState = MutableStateFlow<UiState<LogoutResponse>>(UiState.Empty)
    val logoutState: StateFlow<UiState<LogoutResponse>> = _logoutState

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

    fun refreshToken(request: RefreshTokenRequest) {
        executeWithUiState(_refreshTokenState) {
            authRepository.refreshToken(request)
        }
    }

    fun forgotPassword(request: ForgotPasswordRequest) {
        executeWithUiState(_forgotPasswordState) {
            authRepository.forgotPassword(request)
        }
    }

    fun resetPassword(request: ResetPasswordRequest) {
        executeWithUiState(_resetPasswordState) {
            authRepository.resetPassword(request)
        }
    }

    fun logout(request: LogoutRequest) {
        executeWithUiState(_logoutState) {
            authRepository.logout(request)
        }
    }

    fun resetAuthStates() {
        _userLoginState.value = UiState.Empty
        _vendorLoginState.value = UiState.Empty
        _userRegisterState.value = UiState.Empty
        _vendorRegisterState.value = UiState.Empty
        _refreshTokenState.value = UiState.Empty
        _forgotPasswordState.value = UiState.Empty
        _resetPasswordState.value = UiState.Empty
        _logoutState.value = UiState.Empty
    }

}