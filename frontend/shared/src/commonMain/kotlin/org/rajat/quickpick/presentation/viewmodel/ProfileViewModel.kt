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
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private val logger = Logger.withTag("CLOUDINARY_IMAGE_DEBUG")

@OptIn(ExperimentalTime::class)
class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val imageUploadRepository: ImageUploadRepository
) : ViewModel() {

    private val cacheLogger = Logger.withTag("ProfileViewModel_Cache")

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

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private var cachedStudentProfile: GetStudentProfileResponse? = null
    private var studentProfileCacheTime: Long = 0
    private var cachedVendorProfile: GetVendorProfileResponse? = null
    private var vendorProfileCacheTime: Long = 0
    private val cacheValidityDuration = 60_000L

    init {
        cacheLogger.d { "ProfileViewModel instance created: ${this.hashCode()}" }
    }

    private fun isCacheValid(cacheTime: Long): Boolean {
        return (Clock.System.now().toEpochMilliseconds() - cacheTime) < cacheValidityDuration
    }

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

    private var isStudentProfileLoading = false

    fun getStudentProfile(forceRefresh: Boolean = false) {
        cacheLogger.d { "getStudentProfile called - forceRefresh=$forceRefresh, cachedData=${cachedStudentProfile != null}, cacheValid=${if (cachedStudentProfile != null) isCacheValid(studentProfileCacheTime) else false}, currentState=${_studentProfileState.value::class.simpleName}, isLoading=$isStudentProfileLoading" }

        if (isStudentProfileLoading && !forceRefresh) {
            cacheLogger.d { "⏳ REQUEST IN FLIGHT - Skipping duplicate call" }
            return
        }

        if (!forceRefresh && _studentProfileState.value is UiState.Success && cachedStudentProfile != null && isCacheValid(studentProfileCacheTime)) {
            cacheLogger.d { "✅ CACHE HIT - State already Success, returning without API call" }
            return
        }

        if (!forceRefresh && cachedStudentProfile != null && isCacheValid(studentProfileCacheTime)) {
            cacheLogger.d { "✅ CACHE HIT - Setting state to cached Success" }
            _studentProfileState.value = UiState.Success(cachedStudentProfile!!)
            return
        }

        cacheLogger.d { "❌ CACHE MISS - Making API call" }
        isStudentProfileLoading = true
        viewModelScope.launch {
            if (forceRefresh) {
                _isRefreshing.value = true
            } else {
                _studentProfileState.value = UiState.Loading
            }

            val result = profileRepository.getStudentProfile()
            result.fold(
                onSuccess = { data ->
                    cachedStudentProfile = data
                    studentProfileCacheTime = Clock.System.now().toEpochMilliseconds()
                    _studentProfileState.value = UiState.Success(data)
                    cacheLogger.d { "API call successful, data cached" }
                },
                onFailure = { error ->
                    _studentProfileState.value = UiState.Error(error.message ?: "Unknown error")
                    cacheLogger.e { "API call failed: ${error.message}" }
                }
            )
            _isRefreshing.value = false
            isStudentProfileLoading = false
        }
    }

    private var isVendorProfileLoading = false

    fun getVendorProfile(forceRefresh: Boolean = false) {
        cacheLogger.d { "getVendorProfile called - forceRefresh=$forceRefresh, isLoading=$isVendorProfileLoading" }

        if (isVendorProfileLoading && !forceRefresh) {
            cacheLogger.d { "⏳ REQUEST IN FLIGHT - Skipping duplicate call" }
            return
        }

        if (!forceRefresh && _vendorProfileState.value is UiState.Success && cachedVendorProfile != null && isCacheValid(vendorProfileCacheTime)) {
            cacheLogger.d { "✅ CACHE HIT - State already Success, returning without API call" }
            return
        }

        if (!forceRefresh && cachedVendorProfile != null && isCacheValid(vendorProfileCacheTime)) {
            _vendorProfileState.value = UiState.Success(cachedVendorProfile!!)
            return
        }

        isVendorProfileLoading = true
        viewModelScope.launch {
            if (forceRefresh) {
                _isRefreshing.value = true
            } else {
                _vendorProfileState.value = UiState.Loading
            }

            val result = profileRepository.getVendorProfile()
            result.fold(
                onSuccess = { data ->
                    cachedVendorProfile = data
                    vendorProfileCacheTime = Clock.System.now().toEpochMilliseconds()
                    _vendorProfileState.value = UiState.Success(data)
                },
                onFailure = { error ->
                    _vendorProfileState.value = UiState.Error(error.message ?: "Unknown error")
                }
            )
            _isRefreshing.value = false
            isVendorProfileLoading = false
        }
    }

    fun checkVendorVerificationStatus() {
        executeWithUiState(_vendorVerificationStatusState) {
            profileRepository.getVendorVerificationStatus()
        }
    }

    fun updateStudentProfile(request: UpdateUserProfileRequest) {
        viewModelScope.launch {
            _updateStudentProfileState.value = UiState.Loading
            val result = profileRepository.updateStudentProfile(request)
            result.fold(
                onSuccess = { data ->
                    cachedStudentProfile = null
                    studentProfileCacheTime = 0
                    _updateStudentProfileState.value = UiState.Success(data)
                },
                onFailure = { error ->
                    _updateStudentProfileState.value = UiState.Error(error.message ?: "Unknown error")
                }
            )
        }
    }

    fun updateVendorProfile(request: UpdateVendorProfileRequest) {
        viewModelScope.launch {
            _updateVendorProfileState.value = UiState.Loading
            val result = profileRepository.updateVendorProfile(request)
            result.fold(
                onSuccess = { data ->
                    cachedVendorProfile = null
                    vendorProfileCacheTime = 0
                    _updateVendorProfileState.value = UiState.Success(data)
                },
                onFailure = { error ->
                    _updateVendorProfileState.value = UiState.Error(error.message ?: "Unknown error")
                }
            )
        }
    }

    fun invalidateStudentProfileCache() {
        cachedStudentProfile = null
        studentProfileCacheTime = 0
    }

    fun invalidateVendorProfileCache() {
        cachedVendorProfile = null
        vendorProfileCacheTime = 0
    }

    fun resetProfileStates() {
        _studentProfileState.value = UiState.Empty
        _vendorProfileState.value = UiState.Empty
        _vendorVerificationStatusState.value = UiState.Empty
        _updateStudentProfileState.value = UiState.Empty
        _updateVendorProfileState.value = UiState.Empty
        cachedStudentProfile = null
        cachedVendorProfile = null
        studentProfileCacheTime = 0
        vendorProfileCacheTime = 0
    }
}
