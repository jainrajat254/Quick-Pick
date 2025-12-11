package org.rajat.quickpick.data.repository

import org.rajat.quickpick.domain.modal.admin.AdminCreateRequest
import org.rajat.quickpick.domain.modal.admin.AdminCreateResponse
import org.rajat.quickpick.domain.modal.admin.AdminLoginRequest
import org.rajat.quickpick.domain.modal.admin.AdminLoginResponse
import org.rajat.quickpick.domain.repository.AdminRepository
import org.rajat.quickpick.domain.service.AdminApiService

class AdminRepositoryImpl(private val adminApiService: AdminApiService) : AdminRepository {

    override suspend fun adminLogin(adminLoginRequest: AdminLoginRequest): Result<AdminLoginResponse> {
        return runCatching {
            adminApiService.adminLogin(adminLoginRequest = adminLoginRequest)
        }
    }

    override suspend fun adminCreate(adminCreateRequest: AdminCreateRequest): Result<AdminCreateResponse> {
        return runCatching {
            adminApiService.adminCreate(adminCreateRequest = adminCreateRequest)
        }
    }

}