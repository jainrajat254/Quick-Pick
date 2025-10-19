package org.rajat.quickpick.domain.repository

import org.rajat.quickpick.domain.modal.auth.LoginUserRequest
import org.rajat.quickpick.domain.modal.auth.LoginUserResponse
import org.rajat.quickpick.domain.modal.auth.LoginVendorRequest
import org.rajat.quickpick.domain.modal.auth.LoginVendorResponse
import org.rajat.quickpick.domain.modal.auth.RegisterUserRequest
import org.rajat.quickpick.domain.modal.auth.RegisterUserResponse
import org.rajat.quickpick.domain.modal.auth.RegisterVendorRequest
import org.rajat.quickpick.domain.modal.auth.RegisterVendorResponse

interface AuthRepository {
    suspend fun userLogin(loginUserRequest: LoginUserRequest): Result<LoginUserResponse>
    suspend fun vendorLogin(loginVendorRequest: LoginVendorRequest): Result<LoginVendorResponse>
    suspend fun userRegister(registerUserRequest: RegisterUserRequest): Result<RegisterUserResponse>
    suspend fun vendorRegister(registerVendorRequest: RegisterVendorRequest): Result<RegisterVendorResponse>
}
