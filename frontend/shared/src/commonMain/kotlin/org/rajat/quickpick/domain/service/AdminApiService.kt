package org.rajat.quickpick.domain.service

import org.rajat.quickpick.domain.modal.admin.AdminCreateRequest
import org.rajat.quickpick.domain.modal.admin.AdminCreateResponse
import org.rajat.quickpick.domain.modal.admin.AdminLoginRequest
import org.rajat.quickpick.domain.modal.admin.AdminLoginResponse

interface AdminApiService {

    suspend fun adminLogin(adminLoginRequest: AdminLoginRequest): AdminLoginResponse
    suspend fun adminCreate(adminCreateRequest: AdminCreateRequest): AdminCreateResponse
}