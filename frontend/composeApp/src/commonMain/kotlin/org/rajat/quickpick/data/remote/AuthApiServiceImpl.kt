package org.rajat.quickpick.data.remote

import io.ktor.client.HttpClient
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
import org.rajat.quickpick.domain.service.AuthApiService
import org.rajat.quickpick.utils.Constants
import org.rajat.quickpick.utils.network.safePost

class AuthApiServiceImpl(private val httpClient: HttpClient) : AuthApiService {

    override suspend fun userLogin(loginUserRequest: LoginUserRequest): LoginUserResponse {
        return httpClient.safePost(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.USER_LOGIN}",
            body = loginUserRequest
        )
    }

    override suspend fun vendorLogin(loginVendorRequest: LoginVendorRequest): LoginVendorResponse {
        return httpClient.safePost(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.VENDOR_LOGIN}",
            body = loginVendorRequest
        )
    }

    override suspend fun userRegister(registerUserRequest: RegisterUserRequest): LoginUserResponse {
        return httpClient.safePost(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.USER_REGISTER}",
            body = registerUserRequest
        )
    }

    override suspend fun vendorRegister(registerVendorRequest: RegisterVendorRequest): LoginVendorResponse {
        return httpClient.safePost(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.VENDOR_REGISTER}",
            body = registerVendorRequest
        )
    }

    override suspend fun refreshToken(refreshTokenRequest: RefreshTokenRequest): RefreshTokenResponse {
        return httpClient.safePost(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.REFRESH_TOKEN}",
            body = refreshTokenRequest
        )
    }

    override suspend fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest): ForgotPasswordResponse {
        return httpClient.safePost(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.FORGOT_PASSWORD}",
            body = forgotPasswordRequest
        )
    }

    override suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): ResetPasswordResponse {
        return httpClient.safePost(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.RESET_PASSWORD}",
            body = resetPasswordRequest
        )
    }

    override suspend fun logout(logoutRequest: LogoutRequest): LogoutResponse {
        return httpClient.safePost(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.LOGOUT}",
            body = logoutRequest
        )
    }
}
