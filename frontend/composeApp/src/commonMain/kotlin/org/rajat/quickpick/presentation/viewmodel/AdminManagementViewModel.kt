package org.rajat.quickpick.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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
import org.rajat.quickpick.utils.UiState

class AdminManagementViewModel(
    private val repository: AdminManagementRepository
) : ViewModel() {

    private val _verifyVendorState = MutableStateFlow<UiState<VerifyVendorResponse>>(UiState.Empty)
    val verifyVendorState: StateFlow<UiState<VerifyVendorResponse>> = _verifyVendorState

    private val _rejectVendorState = MutableStateFlow<UiState<RejectVendorResponse>>(UiState.Empty)
    val rejectVendorState: StateFlow<UiState<RejectVendorResponse>> = _rejectVendorState

    private val _suspendUserState = MutableStateFlow<UiState<SuspendUserResponse>>(UiState.Empty)
    val suspendUserState: StateFlow<UiState<SuspendUserResponse>> = _suspendUserState

    private val _suspendVendorState =
        MutableStateFlow<UiState<SuspendVendorResponse>>(UiState.Empty)
    val suspendVendorState: StateFlow<UiState<SuspendVendorResponse>> = _suspendVendorState

    private val _unsuspendUserState =
        MutableStateFlow<UiState<UnsuspendUserResponse>>(UiState.Empty)
    val unsuspendUserState: StateFlow<UiState<UnsuspendUserResponse>> = _unsuspendUserState

    private val _unsuspendVendorState =
        MutableStateFlow<UiState<UnsuspendVendorResponse>>(UiState.Empty)
    val unsuspendVendorState: StateFlow<UiState<UnsuspendVendorResponse>> = _unsuspendVendorState

    private val _getAllUsersState = MutableStateFlow<UiState<GetAllUsersResponse>>(UiState.Empty)
    val getAllUsersState: StateFlow<UiState<GetAllUsersResponse>> = _getAllUsersState

    private val _getAllVendorsState =
        MutableStateFlow<UiState<GetAllVendorsResponse>>(UiState.Empty)
    val getAllVendorsState: StateFlow<UiState<GetAllVendorsResponse>> = _getAllVendorsState

    private val _getPendingVendorsState =
        MutableStateFlow<UiState<GetPendingVendorsResponse>>(UiState.Empty)
    val getPendingVendorsState: StateFlow<UiState<GetPendingVendorsResponse>> =
        _getPendingVendorsState

    private val _getUsersByCollegeState =
        MutableStateFlow<UiState<GetUsersbyCollegeResponse>>(UiState.Empty)
    val getUsersByCollegeState: StateFlow<UiState<GetUsersbyCollegeResponse>> =
        _getUsersByCollegeState

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

    fun verifyVendor(vendorId: String, request: VerifyVendorRequest) {
        executeWithUiState(_verifyVendorState) {
            repository.verifyVendor(vendorId, request)
        }
    }

    fun rejectVendor(vendorId: String, request: RejectVendorRequest) {
        executeWithUiState(_rejectVendorState) {
            repository.rejectVendor(vendorId, request)
        }
    }

    fun suspendUser(userId: String, request: SuspendUserRequest) {
        executeWithUiState(_suspendUserState) {
            repository.suspendUser(userId, request)
        }
    }

    fun suspendVendor(vendorId: String, request: SuspendVendorRequest) {
        executeWithUiState(_suspendVendorState) {
            repository.suspendVendor(vendorId, request)
        }
    }

    fun unSuspendUser(userId: String) {
        executeWithUiState(_unsuspendUserState) {
            repository.unSuspendUser(userId)
        }
    }

    fun unSuspendVendor(vendorId: String) {
        executeWithUiState(_unsuspendVendorState) {
            repository.unSuspendVendor(vendorId)
        }
    }

    fun getAllUsers(page: Int, size: Int) {
        executeWithUiState(_getAllUsersState) {
            repository.getAllUsers(page, size)
        }
    }

    fun getAllVendors(page: Int, size: Int) {
        executeWithUiState(_getAllVendorsState) {
            repository.getAllVendors(page, size)
        }
    }

    fun getPendingVendors(page: Int, size: Int) {
        executeWithUiState(_getPendingVendorsState) {
            repository.getPendingVendors(page, size)
        }
    }

    fun getUsersByCollege(collegeId: String, page: Int, size: Int) {
        executeWithUiState(_getUsersByCollegeState) {
            repository.getUsersByCollege(collegeId, page, size)
        }
    }

    fun resetAdminManagementStates() {
        _verifyVendorState.value = UiState.Empty
        _rejectVendorState.value = UiState.Empty
        _suspendUserState.value = UiState.Empty
        _suspendVendorState.value = UiState.Empty
        _unsuspendUserState.value = UiState.Empty
        _unsuspendVendorState.value = UiState.Empty
        _getAllUsersState.value = UiState.Empty
        _getAllVendorsState.value = UiState.Empty
        _getPendingVendorsState.value = UiState.Empty
        _getUsersByCollegeState.value = UiState.Empty
    }
}
