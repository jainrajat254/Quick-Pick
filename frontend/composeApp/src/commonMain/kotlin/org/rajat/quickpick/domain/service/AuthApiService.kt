package org.rajat.quickpick.domain.service

import org.rajat.quickpick.domain.modal.auth.LoginUserRequest
import org.rajat.quickpick.domain.modal.auth.LoginUserResponse
import org.rajat.quickpick.domain.modal.auth.LoginVendorResponse
import org.rajat.quickpick.domain.modal.auth.LoginVendorRequest
import org.rajat.quickpick.domain.modal.auth.RegisterUserRequest
import org.rajat.quickpick.domain.modal.auth.RegisterUserResponse
import org.rajat.quickpick.domain.modal.auth.RegisterVendorRequest
import org.rajat.quickpick.domain.modal.auth.RegisterVendorResponse

interface AuthApiService {

    suspend fun userLogin(loginUserRequest: LoginUserRequest): LoginUserResponse
    suspend fun vendorLogin(loginVendorRequest: LoginVendorRequest): LoginVendorResponse
    suspend fun userRegister(registerUserRequest: RegisterUserRequest): RegisterUserResponse
    suspend fun vendorRegister(registerVendorRequest: RegisterVendorRequest): RegisterVendorResponse
}