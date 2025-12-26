package org.rajat.quickpick.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.rajat.quickpick.domain.modal.profile.GetStudentProfileResponse
import org.rajat.quickpick.domain.modal.profile.GetVendorProfileResponse
import org.rajat.quickpick.domain.modal.profile.UpdateUserProfileRequest
import org.rajat.quickpick.domain.modal.profile.UpdateUserProfileResponse
import org.rajat.quickpick.domain.modal.profile.UpdateVendorProfileRequest
import org.rajat.quickpick.domain.modal.profile.UpdateVendorProfileResponse
import org.rajat.quickpick.domain.modal.profile.VendorVerificationStatusResponse
import org.rajat.quickpick.domain.repository.ImageUploadRepository
import org.rajat.quickpick.domain.repository.ProfileRepository
import org.rajat.quickpick.utils.ImageUploadState
import org.rajat.quickpick.utils.UiState

private val logger = Logger.withTag("CLOUDINARY_IMAGE_DEBUG")

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val imageUploadRepository: ImageUploadRepository
) : ViewModel() {

    private val _studentProfileState =
        MutableStateFlow<UiState<GetStudentProfileResponse>>(UiState.Empty)
    val studentProfileState: StateFlow<UiState<GetStudentProfileResponse>> = _studentProfileState

    private val _vendorProfileState =
        MutableStateFlow<UiState<GetVendorProfileResponse>>(UiState.Empty)
    val vendorProfileState: StateFlow<UiState<GetVendorProfileResponse>> = _vendorProfileState

    private val _vendorVerificationStatusState =
        MutableStateFlow<UiState<VendorVerificationStatusResponse>>(UiState.Empty)
    val vendorVerificationStatusState: StateFlow<UiState<VendorVerificationStatusResponse>> = _vendorVerificationStatusState

    private val _updateStudentProfileState =
        MutableStateFlow<UiState<UpdateUserProfileResponse>>(UiState.Empty)
    val updateStudentProfileState: StateFlow<UiState<UpdateUserProfileResponse>> =
        _updateStudentProfileState

    private val _updateVendorProfileState =
        MutableStateFlow<UiState<UpdateVendorProfileResponse>>(UiState.Empty)
    val updateVendorProfileState: StateFlow<UiState<UpdateVendorProfileResponse>> =
        _updateVendorProfileState

    private val _imageUploadState = MutableStateFlow<ImageUploadState>(ImageUploadState.Idle)
    val imageUploadState: StateFlow<ImageUploadState> = _imageUploadState

    private val _uploadedImageUrl = MutableStateFlow<String?>(null)
    val uploadedImageUrl: StateFlow<String?> = _uploadedImageUrl

    private fun <T> executeWithUiState(
        stateFlow: MutableStateFlow<UiState<T>>,
        block: suspend () -> Result<T>
    ) {
        viewModelScope.launch {
            stateFlow.value = UiState.Loading
            val result = block()
            stateFlow.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun uploadProfileImage(imageBytes: ByteArray, fileName: String) {
        logger.d { "uploadProfileImage: Called with fileName=$fileName, size=${imageBytes.size} bytes" }
        viewModelScope.launch {
            _imageUploadState.value = ImageUploadState.Uploading
            logger.d { "uploadProfileImage: State set to Uploading" }

            logger.d { "uploadProfileImage: Calling imageUploadRepository.uploadImage" }
            imageUploadRepository.uploadImage(imageBytes, fileName)
                .onSuccess { url ->
                    logger.d { "uploadProfileImage: Upload successful! URL=$url" }
                    _uploadedImageUrl.value = url
                    _imageUploadState.value = ImageUploadState.Success(url)
                    logger.d { "uploadProfileImage: State set to Success with URL=$url" }
                }
                .onFailure { error ->
                    logger.e(error) { "uploadProfileImage: Upload failed with error=${error.message}" }
                    _imageUploadState.value = ImageUploadState.Error(error.message ?: "Upload failed")
                    logger.d { "uploadProfileImage: State set to Error" }
                }
        }
    }

    fun clearUploadedImage() {
        logger.d { "clearUploadedImage: Clearing uploaded image state" }
        _uploadedImageUrl.value = null
        _imageUploadState.value = ImageUploadState.Idle
        logger.d { "clearUploadedImage: State cleared and set to Idle" }
    }

    fun getStudentProfile() {
        executeWithUiState(_studentProfileState) {
            profileRepository.getStudentProfile()
        }
    }

    fun getVendorProfile() {
        executeWithUiState(_vendorProfileState) {
            profileRepository.getVendorProfile()
        }
    }

    fun checkVendorVerificationStatus() {
        executeWithUiState(_vendorVerificationStatusState) {
            profileRepository.getVendorVerificationStatus()
        }
    }

    fun updateStudentProfile(request: UpdateUserProfileRequest) {
        executeWithUiState(_updateStudentProfileState) {
            profileRepository.updateStudentProfile(request)
        }
    }

    fun updateVendorProfile(request: UpdateVendorProfileRequest) {
        executeWithUiState(_updateVendorProfileState) {
            profileRepository.updateVendorProfile(request)
        }
    }

    fun resetProfileStates() {
        _studentProfileState.value = UiState.Empty
        _vendorProfileState.value = UiState.Empty
        _vendorVerificationStatusState.value = UiState.Empty
        _updateStudentProfileState.value = UiState.Empty
        _updateVendorProfileState.value = UiState.Empty
    }
}
