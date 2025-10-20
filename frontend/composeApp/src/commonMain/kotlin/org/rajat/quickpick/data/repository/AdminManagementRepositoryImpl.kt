package org.rajat.quickpick.data.repository

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
import org.rajat.quickpick.domain.repository.AdminManagementRepository
import org.rajat.quickpick.domain.service.AdminManagementApiService

class AdminManagementRepositoryImpl(private val adminManagementApiService: AdminManagementApiService) :
    AdminManagementRepository {
    override suspend fun rejectVendor(
        vendorId: String,
        rejectVendorRequest: RejectVendorRequest
    ): Result<RejectVendorResponse> {
        return runCatching {
            adminManagementApiService.rejectVendor(
                vendorId = vendorId,
                rejectVendorRequest = rejectVendorRequest
            )
        }
    }

    override suspend fun suspendUser(
        userId: String,
        suspendUserRequest: SuspendUserRequest
    ): Result<SuspendUserResponse> {
        return runCatching {
            adminManagementApiService.suspendUser(
                userId = userId,
                suspendUserRequest = suspendUserRequest
            )
        }
    }

    override suspend fun suspendVendor(
        vendorId: String,
        suspendVendorRequest: SuspendVendorRequest
    ): Result<SuspendVendorResponse> {
        return runCatching {
            adminManagementApiService.suspendVendor(
                vendorId = vendorId,
                suspendVendorRequest = suspendVendorRequest
            )
        }
    }

    override suspend fun unSuspendUser(userId: String): Result<UnsuspendUserResponse> {
        return runCatching {
            adminManagementApiService.unSuspendUser(
                userId = userId
            )
        }
    }

    override suspend fun unSuspendVendor(vendorId: String): Result<UnsuspendVendorResponse> {
        return runCatching {
            adminManagementApiService.unSuspendVendor(
                vendorId = vendorId
            )
        }
    }

    override suspend fun verifyVendor(
        vendorId: String,
        verifyVendorRequest: VerifyVendorRequest
    ): Result<VerifyVendorResponse> {
        return runCatching {
            adminManagementApiService.verifyVendor(
                vendorId = vendorId,
                verifyVendorRequest = verifyVendorRequest
            )
        }
    }

    override suspend fun getAllUsers(
        page: Int,
        size: Int
    ): Result<GetAllUsersResponse> {
        return runCatching {
            adminManagementApiService.getAllUsers(
                page = page,
                size = size
            )
        }
    }

    override suspend fun getAllVendors(
        page: Int,
        size: Int
    ): Result<GetAllVendorsResponse> {
        return runCatching {
            adminManagementApiService.getAllVendors(
                page = page,
                size = size
            )
        }
    }

    override suspend fun getPendingVendors(
        page: Int,
        size: Int
    ): Result<GetPendingVendorsResponse> {
        return runCatching {
            adminManagementApiService.getPendingVendors(
                page = page,
                size = size
            )
        }
    }

    override suspend fun getUsersByCollege(
        collegeId: String,
        page: Int,
        size: Int
    ): Result<GetUsersbyCollegeResponse> {
        return runCatching {
            adminManagementApiService.getUsersByCollege(
                collegeId = collegeId,
                page = page,
                size = size
            )
        }
    }
}