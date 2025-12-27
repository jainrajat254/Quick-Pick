package org.rajat.quickpick.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.rajat.quickpick.data.local.LocalDataStore
import org.rajat.quickpick.domain.modal.auth.ChangePasswordRequest
import org.rajat.quickpick.domain.modal.auth.ChangePasswordResponse
import org.rajat.quickpick.domain.modal.auth.EmailOtpRequest
import org.rajat.quickpick.domain.modal.auth.EmailOtpVerifyRequest
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
import org.rajat.quickpick.domain.modal.auth.RegisterVendorRequest
import org.rajat.quickpick.domain.modal.auth.ResetPasswordRequest
import org.rajat.quickpick.domain.modal.auth.ResetPasswordResponse
import org.rajat.quickpick.domain.modal.auth.SimpleMessageResponse
import org.rajat.quickpick.domain.modal.auth.PasswordOtpRequest
import org.rajat.quickpick.domain.modal.auth.ResetPasswordOtpRequest
import org.rajat.quickpick.domain.repository.AuthRepository
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.di.TokenProvider
import org.rajat.quickpick.fcm.FcmPlatformManager
import org.rajat.quickpick.utils.tokens.PlatformScheduler
import org.rajat.quickpick.utils.websocket.VendorWebSocketManager

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val dataStore: LocalDataStore
) : ViewModel() {

    private val _userLoginState = MutableStateFlow<UiState<LoginUserResponse>>(UiState.Empty)
    val userLoginState: StateFlow<UiState<LoginUserResponse>> = _userLoginState

    private val _vendorLoginState = MutableStateFlow<UiState<LoginVendorResponse>>(UiState.Empty)
    val vendorLoginState: StateFlow<UiState<LoginVendorResponse>> = _vendorLoginState

    private val _userRegisterState = MutableStateFlow<UiState<LoginUserResponse>>(UiState.Empty)
    val userRegisterState: StateFlow<UiState<LoginUserResponse>> = _userRegisterState

    private val _vendorRegisterState =
        MutableStateFlow<UiState<LoginVendorResponse>>(UiState.Empty)
    val vendorRegisterState: StateFlow<UiState<LoginVendorResponse>> = _vendorRegisterState

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

    private val _changePasswordState = MutableStateFlow<UiState<ChangePasswordResponse>>(UiState.Empty)
    val changePasswordState: StateFlow<UiState<ChangePasswordResponse>> = _changePasswordState

    private val _sendEmailOtpState = MutableStateFlow<UiState<SimpleMessageResponse>>(UiState.Empty)
    val sendEmailOtpState: StateFlow<UiState<SimpleMessageResponse>> = _sendEmailOtpState

    private val _verifyEmailOtpState = MutableStateFlow<UiState<SimpleMessageResponse>>(UiState.Empty)
    val verifyEmailOtpState: StateFlow<UiState<SimpleMessageResponse>> = _verifyEmailOtpState

    private val _sendPasswordOtpState = MutableStateFlow<UiState<SimpleMessageResponse>>(UiState.Empty)
    val sendPasswordOtpState: StateFlow<UiState<SimpleMessageResponse>> = _sendPasswordOtpState
    private val _resetPasswordOtpState = MutableStateFlow<UiState<SimpleMessageResponse>>(UiState.Empty)
    val resetPasswordOtpState: StateFlow<UiState<SimpleMessageResponse>> = _resetPasswordOtpState

    private var pendingEmail: String? = null
    private var pendingPassword: String? = null
    private var pendingUserType: String? = null

    fun setPendingRegistrationCredentials(email: String, password: String, userType: String) {
        pendingEmail = email
        pendingPassword = password
        pendingUserType = userType
    }

    fun consumePendingRegistrationCredentials(): Triple<String, String, String>? {
        val triple = if (pendingEmail != null && pendingPassword != null && pendingUserType != null) {
            Triple(pendingEmail!!, pendingPassword!!, pendingUserType!!)
        } else null
        pendingEmail = null
        pendingPassword = null
        pendingUserType = null
        return triple
    }

    fun hasPendingRegistrationCredentials(): Boolean = pendingEmail != null && pendingPassword != null && pendingUserType != null

    fun clearPendingRegistrationCredentials() {
        pendingEmail = null
        pendingPassword = null
        pendingUserType = null
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
        viewModelScope.launch {
            _logoutState.value = UiState.Loading

            val authToken = try { dataStore.getToken() } catch (_: Exception) { null }
            authToken?.let { token ->
                try {
                    FcmPlatformManager.removeTokenFromServer(token)
                } catch (_: Exception) {
                }
            }

            val apiResult: Result<LogoutResponse> = try {
                authRepository.logout(request)
            } catch (e: Exception) {
                Result.failure(e)
            }

            try {
                resetAuthStates()

                try { VendorWebSocketManager.disconnect() } catch (_: Exception) {}

                try { PlatformScheduler.cancelScheduledRefresh() } catch (_: Exception) {}

                try { dataStore.clearToken() } catch (_: Exception) {}
                try { dataStore.clearRefreshToken() } catch (_: Exception) {}
                try { dataStore.clearUserProfile() } catch (_: Exception) {}
                try { dataStore.clearVendorProfile() } catch (_: Exception) {}
                try { dataStore.clearUserRole() } catch (_: Exception) {}
                try { dataStore.clearPendingVerification() } catch (_: Exception) {}

                try { dataStore.clearAll() } catch (_: Exception) {}

                TokenProvider.token = null
            } catch (_: Exception) {
            }

            _logoutState.value = apiResult.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun changePassword(request: ChangePasswordRequest) {
        executeWithUiState(_changePasswordState) {
            authRepository.changePassword(request)
        }
    }

    fun sendEmailOtp(request: EmailOtpRequest) {
        executeWithUiState(_sendEmailOtpState) {
            authRepository.sendEmailOtp(request)
        }
    }

    fun verifyEmailOtp(request: EmailOtpVerifyRequest) {
        executeWithUiState(_verifyEmailOtpState) {
            authRepository.verifyEmailOtp(request)
        }
    }

    fun sendPasswordOtp(request: PasswordOtpRequest) {
        executeWithUiState(_sendPasswordOtpState) {
            authRepository.sendPasswordOtp(request)
        }
    }

    fun resetPasswordOtp(request: ResetPasswordOtpRequest) {
        executeWithUiState(_resetPasswordOtpState) {
            authRepository.resetPasswordOtp(request)
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
        _changePasswordState.value = UiState.Empty
        _sendEmailOtpState.value = UiState.Empty
        _verifyEmailOtpState.value = UiState.Empty
        _sendPasswordOtpState.value = UiState.Empty
        _resetPasswordOtpState.value = UiState.Empty
    }

    suspend fun isSessionValid(): Boolean {
        return try {
            authRepository.isSessionValid()
        } catch (e: Exception) {
            false
        }
    }

    fun logout() {
        viewModelScope.launch {
            resetAuthStates()
            clearPendingRegistrationCredentials()

            try { VendorWebSocketManager.disconnect() } catch (_: Exception) {}
            try { PlatformScheduler.cancelScheduledRefresh() } catch (_: Exception) {}

            try { dataStore.clearPendingVerification() } catch (_: Exception) {}
            try { dataStore.clearAll() } catch (_: Exception) {}

            TokenProvider.token = null
        }
    }

}