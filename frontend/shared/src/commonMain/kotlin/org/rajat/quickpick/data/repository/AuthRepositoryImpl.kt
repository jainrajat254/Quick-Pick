package org.rajat.quickpick.data.repository

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
import org.rajat.quickpick.domain.service.AuthApiService

class AuthRepositoryImpl(private val authApiService: AuthApiService) : AuthRepository {
    override suspend fun userLogin(loginUserRequest: LoginUserRequest): Result<LoginUserResponse> {
        return runCatching {
            authApiService.userLogin(loginUserRequest = loginUserRequest)
        }
    }

    override suspend fun vendorLogin(loginVendorRequest: LoginVendorRequest): Result<LoginVendorResponse> {
        return runCatching {
            authApiService.vendorLogin(loginVendorRequest = loginVendorRequest)
        }
    }

    override suspend fun userRegister(registerUserRequest: RegisterUserRequest): Result<LoginUserResponse> {
        return runCatching {
            authApiService.userRegister(registerUserRequest = registerUserRequest)
        }
    }

    override suspend fun vendorRegister(registerVendorRequest: RegisterVendorRequest): Result<LoginVendorResponse> {
        return runCatching {
            authApiService.vendorRegister(registerVendorRequest = registerVendorRequest)
        }
    }

    override suspend fun refreshToken(refreshTokenRequest: RefreshTokenRequest): Result<RefreshTokenResponse> {
        return runCatching {
            authApiService.refreshToken(refreshTokenRequest = refreshTokenRequest)
        }
    }

    override suspend fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest): Result<ForgotPasswordResponse> {
        return runCatching {
            authApiService.forgotPassword(forgotPasswordRequest = forgotPasswordRequest)
        }
    }

    override suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Result<ResetPasswordResponse> {
        return runCatching {
            authApiService.resetPassword(resetPasswordRequest = resetPasswordRequest)
        }
    }

    override suspend fun logout(logoutRequest: LogoutRequest): Result<LogoutResponse> {
        return runCatching {
            authApiService.logout(logoutRequest = logoutRequest)
        }
    }

    override suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): Result<ChangePasswordResponse> {
        return runCatching { authApiService.changePassword(changePasswordRequest) }
    }

    override suspend fun sendEmailOtp(request: EmailOtpRequest): Result<SimpleMessageResponse> {
        return runCatching { authApiService.sendEmailOtp(request) }
    }

    override suspend fun verifyEmailOtp(request: EmailOtpVerifyRequest): Result<SimpleMessageResponse> {
        return runCatching { authApiService.verifyEmailOtp(request) }
    }

    override suspend fun sendPasswordOtp(request: PasswordOtpRequest): Result<SimpleMessageResponse> {
        return runCatching { authApiService.sendPasswordOtp(request) }
    }

    override suspend fun resetPasswordOtp(request: ResetPasswordOtpRequest): Result<SimpleMessageResponse> {
        return runCatching { authApiService.resetPasswordOtp(request) }
    }

    override suspend fun isSessionValid(): Boolean {
        return try {
            val response = authApiService.isSessionValid()
            response.success && response.statusCode == 200
        } catch (e: Exception) {
            false
        }
    }
}