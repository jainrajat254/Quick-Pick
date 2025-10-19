package org.rajat.quickpick.data.remote

import io.ktor.client.HttpClient
import org.rajat.quickpick.domain.modal.adminManagement.RejectVendorRequest
import org.rajat.quickpick.domain.modal.adminManagement.RejectVendorResponse
import org.rajat.quickpick.domain.modal.adminManagement.SuspendUserRequest
import org.rajat.quickpick.domain.modal.adminManagement.SuspendUserResponse
import org.rajat.quickpick.domain.modal.adminManagement.SuspendVendorRequest
import org.rajat.quickpick.domain.modal.adminManagement.SuspendVendorResponse
import org.rajat.quickpick.domain.modal.adminManagement.UnsuspendUserResponse
import org.rajat.quickpick.domain.modal.adminManagement.UnsuspendVendorResponse
import org.rajat.quickpick.domain.modal.adminManagement.VerifyVendorRequest
import org.rajat.quickpick.domain.modal.adminManagement.VerifyVendorResponse
import org.rajat.quickpick.domain.modal.adminManagement.getAllUsers.GetAllUsersResponse
import org.rajat.quickpick.domain.modal.adminManagement.getAllVendors.GetAllVendorsResponse
import org.rajat.quickpick.domain.modal.adminManagement.getPendingVendors.GetPendingVendorsResponse
import org.rajat.quickpick.domain.modal.adminManagement.getUsersbyCollege.GetUsersbyCollegeResponse
import org.rajat.quickpick.domain.service.AdminManagementApiService
import org.rajat.quickpick.utils.Constants
import org.rajat.quickpick.utils.network.safeGet
import org.rajat.quickpick.utils.network.safePost

class AdminManagementApiServiceImpl(private val httpClient: HttpClient) :
    AdminManagementApiService {

    override suspend fun rejectVendor(
        vendorId: String,
        rejectVendorRequest: RejectVendorRequest
    ): RejectVendorResponse {
        return httpClient.safePost(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.VENDORS}/$vendorId/reject",
            body = rejectVendorRequest
        )
    }

    override suspend fun suspendUser(
        userId: String,
        suspendUserRequest: SuspendUserRequest
    ): SuspendUserResponse {
        return httpClient.safePost(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.USERS}/$userId/suspend",
            body = suspendUserRequest
        )
    }

    override suspend fun suspendVendor(
        vendorId: String,
        suspendVendorRequest: SuspendVendorRequest
    ): SuspendVendorResponse {
        return httpClient.safePost(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.VENDORS}/$vendorId/suspend",
            body = suspendVendorRequest
        )
    }

    override suspend fun unSuspendUser(userId: String): UnsuspendUserResponse {
        return httpClient.safePost(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.USERS}/$userId/unsuspend"
        )
    }

    override suspend fun unSuspendVendor(vendorId: String): UnsuspendVendorResponse {
        return httpClient.safePost(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.VENDORS}/$vendorId/unsuspend"
        )
    }

    override suspend fun verifyVendor(
        vendorId: String,
        verifyVendorRequest: VerifyVendorRequest
    ): VerifyVendorResponse {
        return httpClient.safePost(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.VENDORS}/$vendorId/verify",
            body = verifyVendorRequest
        )
    }

    override suspend fun getAllUsers(page: Int, size: Int): GetAllUsersResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.USERS}",
            queryParams = mapOf("page" to "$page", "size" to "$size")
        )
    }

    override suspend fun getAllVendors(page: Int, size: Int): GetAllVendorsResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.VENDORS}",
            queryParams = mapOf("page" to "$page", "size" to "$size")
        )
    }

    override suspend fun getPendingVendors(page: Int, size: Int): GetPendingVendorsResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.PENDING_VENDORS}",
            queryParams = mapOf("page" to "$page", "size" to "$size")
        )
    }

    override suspend fun getUsersByCollege(
        collegeId: String,
        page: Int,
        size: Int
    ): GetUsersbyCollegeResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.USERS}/college/$collegeId",
            queryParams = mapOf("page" to "$page", "size" to "$size")
        )
    }
}