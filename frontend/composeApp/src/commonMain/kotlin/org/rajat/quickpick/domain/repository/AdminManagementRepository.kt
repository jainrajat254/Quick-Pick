package org.rajat.quickpick.domain.repository

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

interface AdminManagementRepository {
    suspend fun rejectVendor(vendorId: String, rejectVendorRequest: RejectVendorRequest): Result<RejectVendorResponse>
    suspend fun suspendUser(userId: String,suspendUserRequest: SuspendUserRequest): Result<SuspendUserResponse>
    suspend fun suspendVendor(vendorId: String,suspendVendorRequest: SuspendVendorRequest): Result<SuspendVendorResponse>
    suspend fun unSuspendUser(userId: String): Result<UnsuspendUserResponse>
    suspend fun unSuspendVendor(vendorId: String): Result<UnsuspendVendorResponse>
    suspend fun verifyVendor(vendorId: String, verifyVendorRequest: VerifyVendorRequest): Result<VerifyVendorResponse>
    suspend fun getAllUsers(page: Int, size: Int): Result<GetAllUsersResponse>
    suspend fun getAllVendors(page: Int, size: Int): Result<GetAllVendorsResponse>
    suspend fun getPendingVendors(page: Int, size: Int): Result<GetPendingVendorsResponse>
    suspend fun getUsersByCollege(
        collegeId: String,
        page: Int,
        size: Int
    ): Result<GetUsersbyCollegeResponse>
}