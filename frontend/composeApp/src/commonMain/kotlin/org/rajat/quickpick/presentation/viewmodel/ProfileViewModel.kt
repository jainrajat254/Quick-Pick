package org.rajat.quickpick.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.rajat.quickpick.domain.modal.profile.GetStudentProfileResponse
import org.rajat.quickpick.domain.modal.profile.GetVendorProfileResponse
import org.rajat.quickpick.domain.modal.profile.UpdateUserProfileRequest
import org.rajat.quickpick.domain.modal.profile.UpdateUserProfileResponse
import org.rajat.quickpick.domain.modal.profile.UpdateVendorProfileRequest
import org.rajat.quickpick.domain.modal.profile.UpdateVendorProfileResponse
import org.rajat.quickpick.domain.repository.ProfileRepository
import org.rajat.quickpick.utils.UiState

class ProfileViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _studentProfileState =
        MutableStateFlow<UiState<GetStudentProfileResponse>>(UiState.Empty)
    val studentProfileState: StateFlow<UiState<GetStudentProfileResponse>> = _studentProfileState

    private val _vendorProfileState =
        MutableStateFlow<UiState<GetVendorProfileResponse>>(UiState.Empty)
    val vendorProfileState: StateFlow<UiState<GetVendorProfileResponse>> = _vendorProfileState

    private val _updateStudentProfileState =
        MutableStateFlow<UiState<UpdateUserProfileResponse>>(UiState.Empty)
    val updateStudentProfileState: StateFlow<UiState<UpdateUserProfileResponse>> =
        _updateStudentProfileState

    private val _updateVendorProfileState =
        MutableStateFlow<UiState<UpdateVendorProfileResponse>>(UiState.Empty)
    val updateVendorProfileState: StateFlow<UiState<UpdateVendorProfileResponse>> =
        _updateVendorProfileState

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
        _updateStudentProfileState.value = UiState.Empty
        _updateVendorProfileState.value = UiState.Empty
    }
}
