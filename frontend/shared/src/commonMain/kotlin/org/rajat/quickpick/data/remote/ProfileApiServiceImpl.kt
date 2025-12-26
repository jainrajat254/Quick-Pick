package org.rajat.quickpick.data.remote

import io.ktor.client.HttpClient
import org.rajat.quickpick.domain.modal.profile.GetStudentProfileResponse
import org.rajat.quickpick.domain.modal.profile.GetVendorProfileResponse
import org.rajat.quickpick.domain.modal.profile.UpdateUserProfileRequest
import org.rajat.quickpick.domain.modal.profile.UpdateUserProfileResponse
import org.rajat.quickpick.domain.modal.profile.UpdateVendorProfileRequest
import org.rajat.quickpick.domain.modal.profile.UpdateVendorProfileResponse
import org.rajat.quickpick.domain.modal.profile.VendorVerificationStatusResponse
import org.rajat.quickpick.domain.service.ProfileApiService
import org.rajat.quickpick.utils.Constants
import org.rajat.quickpick.utils.network.safeGet
import org.rajat.quickpick.utils.network.safePut

class ProfileApiServiceImpl(private val httpClient: HttpClient) : ProfileApiService {

    override suspend fun getStudentProfile(): GetStudentProfileResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_STUDENT_PROFILE}",
        )
    }

    override suspend fun getVendorProfile(): GetVendorProfileResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_VENDOR_PROFILE}",
        )
    }

    override suspend fun getVendorVerificationStatus(): VendorVerificationStatusResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.VENDOR_VERIFICATION_STATUS}",
        )
    }

    override suspend fun updateStudentProfile(updateUserProfileRequest: UpdateUserProfileRequest): UpdateUserProfileResponse {
        return httpClient.safePut(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_STUDENT_PROFILE}",
            body = updateUserProfileRequest
        )
    }

    override suspend fun updateVendorProfile(updateVendorProfileRequest: UpdateVendorProfileRequest): UpdateVendorProfileResponse {
        return httpClient.safePut(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_VENDOR_PROFILE}",
            body = updateVendorProfileRequest
        )
    }
}