package org.rajat.quickpick.domain.repository

import org.rajat.quickpick.domain.modal.auth.ChangePasswordRequest
import org.rajat.quickpick.domain.modal.auth.ChangePasswordResponse
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

interface AuthRepository {
    suspend fun userLogin(loginUserRequest: LoginUserRequest): Result<LoginUserResponse>
    suspend fun vendorLogin(loginVendorRequest: LoginVendorRequest): Result<LoginVendorResponse>
    suspend fun userRegister(registerUserRequest: RegisterUserRequest): Result<LoginUserResponse>
    suspend fun vendorRegister(registerVendorRequest: RegisterVendorRequest): Result<LoginVendorResponse>
    suspend fun refreshToken(refreshTokenRequest: RefreshTokenRequest): Result<RefreshTokenResponse>
    suspend fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest): Result<ForgotPasswordResponse>
    suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Result<ResetPasswordResponse>
    suspend fun logout(logoutRequest: LogoutRequest): Result<LogoutResponse>
    suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): Result<ChangePasswordResponse>
}
