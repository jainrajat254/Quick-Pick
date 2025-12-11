package org.rajat.quickpick.domain.repository

import org.rajat.quickpick.domain.modal.profile.GetStudentProfileResponse
import org.rajat.quickpick.domain.modal.profile.GetVendorProfileResponse
import org.rajat.quickpick.domain.modal.profile.UpdateUserProfileRequest
import org.rajat.quickpick.domain.modal.profile.UpdateUserProfileResponse
import org.rajat.quickpick.domain.modal.profile.UpdateVendorProfileRequest
import org.rajat.quickpick.domain.modal.profile.UpdateVendorProfileResponse

interface ProfileRepository {
    suspend fun getStudentProfile(): Result<GetStudentProfileResponse>
    suspend fun getVendorProfile(): Result<GetVendorProfileResponse>
    suspend fun updateStudentProfile(updateUserProfileRequest: UpdateUserProfileRequest): Result<UpdateUserProfileResponse>
    suspend fun updateVendorProfile(updateVendorProfileRequest: UpdateVendorProfileRequest): Result<UpdateVendorProfileResponse>
}