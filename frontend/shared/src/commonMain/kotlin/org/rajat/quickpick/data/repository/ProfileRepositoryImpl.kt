package org.rajat.quickpick.data.repository

import org.rajat.quickpick.domain.modal.profile.GetStudentProfileResponse
import org.rajat.quickpick.domain.modal.profile.GetVendorProfileResponse
import org.rajat.quickpick.domain.modal.profile.UpdateUserProfileRequest
import org.rajat.quickpick.domain.modal.profile.UpdateUserProfileResponse
import org.rajat.quickpick.domain.modal.profile.UpdateVendorProfileRequest
import org.rajat.quickpick.domain.modal.profile.UpdateVendorProfileResponse
import org.rajat.quickpick.domain.repository.ProfileRepository
import org.rajat.quickpick.domain.service.ProfileApiService

class ProfileRepositoryImpl(private val profileApiService: ProfileApiService) : ProfileRepository {

    override suspend fun getStudentProfile(): Result<GetStudentProfileResponse> {
        return runCatching {
            profileApiService.getStudentProfile()
        }
    }

    override suspend fun getVendorProfile(): Result<GetVendorProfileResponse> {
        return runCatching {
            profileApiService.getVendorProfile()
        }
    }

    override suspend fun updateStudentProfile(updateUserProfileRequest: UpdateUserProfileRequest): Result<UpdateUserProfileResponse> {
        return runCatching {
            profileApiService.updateStudentProfile(updateUserProfileRequest = updateUserProfileRequest)
        }
    }

    override suspend fun updateVendorProfile(updateVendorProfileRequest: UpdateVendorProfileRequest): Result<UpdateVendorProfileResponse> {
        return runCatching {
            profileApiService.updateVendorProfile(updateVendorProfileRequest = updateVendorProfileRequest)
        }
    }
}