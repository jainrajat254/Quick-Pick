package org.rajat.quickpick.domain.repository

import org.rajat.quickpick.domain.modal.admin.AdminCreateRequest
import org.rajat.quickpick.domain.modal.admin.AdminCreateResponse
import org.rajat.quickpick.domain.modal.admin.AdminLoginRequest
import org.rajat.quickpick.domain.modal.admin.AdminLoginResponse

interface AdminRepository {
    suspend fun adminLogin(adminLoginRequest: AdminLoginRequest): Result<AdminLoginResponse>
    suspend fun adminCreate(adminCreateRequest: AdminCreateRequest): Result<AdminCreateResponse>
}