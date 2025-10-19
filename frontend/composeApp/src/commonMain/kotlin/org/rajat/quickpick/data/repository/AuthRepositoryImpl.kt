package org.rajat.quickpick.data.repository

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

    override suspend fun userRegister(registerUserRequest: RegisterUserRequest): Result<RegisterUserResponse> {
        return runCatching {
            authApiService.userRegister(registerUserRequest = registerUserRequest)
        }
    }

    override suspend fun vendorRegister(registerVendorRequest: RegisterVendorRequest): Result<RegisterVendorResponse> {
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
}