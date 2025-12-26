package org.rajat.quickpick.domain.service

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
import org.rajat.quickpick.domain.modal.auth.EmailOtpRequest
import org.rajat.quickpick.domain.modal.auth.EmailOtpVerifyRequest
import org.rajat.quickpick.domain.modal.auth.SimpleMessageResponse
import org.rajat.quickpick.domain.modal.auth.PasswordOtpRequest
import org.rajat.quickpick.domain.modal.auth.ResetPasswordOtpRequest
import org.rajat.quickpick.domain.modal.auth.IsSessionValidResponse

interface AuthApiService {

    suspend fun userLogin(loginUserRequest: LoginUserRequest): LoginUserResponse
    suspend fun vendorLogin(loginVendorRequest: LoginVendorRequest): LoginVendorResponse
    suspend fun userRegister(registerUserRequest: RegisterUserRequest): LoginUserResponse
    suspend fun vendorRegister(registerVendorRequest: RegisterVendorRequest): LoginVendorResponse
    suspend fun refreshToken(refreshTokenRequest: RefreshTokenRequest): RefreshTokenResponse
    suspend fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest): ForgotPasswordResponse // reused to send OTP
    suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): ResetPasswordResponse // legacy (token)
    suspend fun logout(logoutRequest: LogoutRequest): LogoutResponse
    suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): ChangePasswordResponse

    suspend fun sendEmailOtp(request: EmailOtpRequest): SimpleMessageResponse
    suspend fun verifyEmailOtp(request: EmailOtpVerifyRequest): SimpleMessageResponse

    suspend fun sendPasswordOtp(request: PasswordOtpRequest): SimpleMessageResponse
    suspend fun resetPasswordOtp(request: ResetPasswordOtpRequest): SimpleMessageResponse

    suspend fun isSessionValid(): IsSessionValidResponse
}