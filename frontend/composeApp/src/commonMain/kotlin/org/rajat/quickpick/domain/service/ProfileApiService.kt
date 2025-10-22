package org.rajat.quickpick.domain.service

import org.rajat.quickpick.domain.modal.profile.GetStudentProfileResponse
import org.rajat.quickpick.domain.modal.profile.GetVendorProfileResponse
import org.rajat.quickpick.domain.modal.profile.UpdateUserProfileRequest
import org.rajat.quickpick.domain.modal.profile.UpdateUserProfileResponse
import org.rajat.quickpick.domain.modal.profile.UpdateVendorProfileRequest
import org.rajat.quickpick.domain.modal.profile.UpdateVendorProfileResponse

interface ProfileApiService {
    suspend fun getStudentProfile(): GetStudentProfileResponse
    suspend fun getVendorProfile(): GetVendorProfileResponse
    suspend fun updateStudentProfile(updateUserProfileRequest: UpdateUserProfileRequest): UpdateUserProfileResponse
    suspend fun updateVendorProfile(updateVendorProfileRequest: UpdateVendorProfileRequest): UpdateVendorProfileResponse
}