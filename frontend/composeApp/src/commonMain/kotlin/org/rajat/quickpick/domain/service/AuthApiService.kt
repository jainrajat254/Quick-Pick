package org.rajat.quickpick.domain.service

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

interface AuthApiService {

    suspend fun userLogin(loginUserRequest: LoginUserRequest): LoginUserResponse
    suspend fun vendorLogin(loginVendorRequest: LoginVendorRequest): LoginVendorResponse
    suspend fun userRegister(registerUserRequest: RegisterUserRequest): LoginUserResponse
    suspend fun vendorRegister(registerVendorRequest: RegisterVendorRequest): LoginVendorResponse
    suspend fun refreshToken(refreshTokenRequest: RefreshTokenRequest): RefreshTokenResponse
    suspend fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest): ForgotPasswordResponse
    suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): ResetPasswordResponse
    suspend fun logout(logoutRequest: LogoutRequest): LogoutResponse
}