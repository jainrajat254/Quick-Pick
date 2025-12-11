package org.rajat.quickpick.domain.service

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

interface AdminManagementApiService {
    suspend fun rejectVendor(
        vendorId: String,
        rejectVendorRequest: RejectVendorRequest
    ): RejectVendorResponse

    suspend fun suspendUser(
        userId: String,
        suspendUserRequest: SuspendUserRequest
    ): SuspendUserResponse

    suspend fun suspendVendor(
        vendorId: String,
        suspendVendorRequest: SuspendVendorRequest
    ): SuspendVendorResponse

    suspend fun unSuspendUser(userId: String): UnsuspendUserResponse
    suspend fun unSuspendVendor(vendorId: String): UnsuspendVendorResponse
    suspend fun verifyVendor(
        vendorId: String,
        verifyVendorRequest: VerifyVendorRequest
    ): VerifyVendorResponse

    suspend fun getAllUsers(page: Int, size: Int): GetAllUsersResponse
    suspend fun getAllVendors(page: Int, size: Int): GetAllVendorsResponse
    suspend fun getPendingVendors(page: Int, size: Int): GetPendingVendorsResponse
    suspend fun getUsersByCollege(
        collegeId: String,
        page: Int,
        size: Int
    ): GetUsersbyCollegeResponse
}