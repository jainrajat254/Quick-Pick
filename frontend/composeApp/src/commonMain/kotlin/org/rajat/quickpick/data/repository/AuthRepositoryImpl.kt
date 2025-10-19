package org.rajat.quickpick.data.repository

import org.rajat.quickpick.domain.modal.auth.LoginUserRequest
import org.rajat.quickpick.domain.modal.auth.LoginUserResponse
import org.rajat.quickpick.domain.modal.auth.LoginVendorRequest
import org.rajat.quickpick.domain.modal.auth.LoginVendorResponse
import org.rajat.quickpick.domain.modal.auth.RegisterUserRequest
import org.rajat.quickpick.domain.modal.auth.RegisterUserResponse
import org.rajat.quickpick.domain.modal.auth.RegisterVendorRequest
import org.rajat.quickpick.domain.modal.auth.RegisterVendorResponse
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
}