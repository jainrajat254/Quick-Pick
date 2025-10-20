package org.rajat.quickpick.data.remote

import io.ktor.client.HttpClient
import org.rajat.quickpick.domain.modal.admin.AdminCreateRequest
import org.rajat.quickpick.domain.modal.admin.AdminCreateResponse
import org.rajat.quickpick.domain.modal.admin.AdminLoginRequest
import org.rajat.quickpick.domain.modal.admin.AdminLoginResponse
import org.rajat.quickpick.domain.service.AdminApiService
import org.rajat.quickpick.utils.Constants
import org.rajat.quickpick.utils.network.safePost

class AdminApiServiceImpl(private val httpClient: HttpClient) : AdminApiService {

    override suspend fun adminLogin(adminLoginRequest: AdminLoginRequest): AdminLoginResponse {
        return httpClient.safePost(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.ADMIN_LOGIN}",
            body = adminLoginRequest
        )
    }

    override suspend fun adminCreate(adminCreateRequest: AdminCreateRequest): AdminCreateResponse {
        return httpClient.safePost(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.ADMIN_CREATE}",
            body = adminCreateRequest
        )
    }
}