package org.rajat.quickpick.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.rajat.quickpick.domain.modal.college.CreateANewCollegeRequest
import org.rajat.quickpick.domain.modal.college.CreateANewCollegeResponse
import org.rajat.quickpick.domain.modal.college.DeleteACollegeResponse
import org.rajat.quickpick.domain.modal.college.GetAllCitiesResponse
import org.rajat.quickpick.domain.modal.college.GetAllStatesResponse
import org.rajat.quickpick.domain.modal.college.GetCollegeByIdResponse
import org.rajat.quickpick.domain.modal.college.GetCollegesByCityResponse
import org.rajat.quickpick.domain.modal.college.GetCollegesByStateResponse
import org.rajat.quickpick.domain.modal.college.GetTotalCollegeCountResponse
import org.rajat.quickpick.domain.modal.college.UpdateAnExistingCollegeRequest
import org.rajat.quickpick.domain.modal.college.paginatedColleges.PaginatedCollegesResponse
import org.rajat.quickpick.domain.repository.CollegeRepository
import org.rajat.quickpick.utils.UiState

class CollegeViewModel(
    private val collegeRepository: CollegeRepository
) : ViewModel() {

    private val _createCollegeState =
        MutableStateFlow<UiState<CreateANewCollegeResponse>>(UiState.Empty)
    val createCollegeState: StateFlow<UiState<CreateANewCollegeResponse>> = _createCollegeState

    private val _deleteCollegeState =
        MutableStateFlow<UiState<DeleteACollegeResponse>>(UiState.Empty)
    val deleteCollegeState: StateFlow<UiState<DeleteACollegeResponse>> = _deleteCollegeState

    private val _updateCollegeState =
        MutableStateFlow<UiState<UpdateAnExistingCollegeRequest>>(UiState.Empty)
    val updateCollegeState: StateFlow<UiState<UpdateAnExistingCollegeRequest>> = _updateCollegeState

    private val _getAllCitiesState = MutableStateFlow<UiState<GetAllCitiesResponse>>(UiState.Empty)
    val getAllCitiesState: StateFlow<UiState<GetAllCitiesResponse>> = _getAllCitiesState

    private val _getAllStatesState = MutableStateFlow<UiState<GetAllStatesResponse>>(UiState.Empty)
    val getAllStatesState: StateFlow<UiState<GetAllStatesResponse>> = _getAllStatesState

    private val _getCollegesByCityState =
        MutableStateFlow<UiState<GetCollegesByCityResponse>>(UiState.Empty)
    val getCollegesByCityState: StateFlow<UiState<GetCollegesByCityResponse>> =
        _getCollegesByCityState

    private val _getCollegesByStateState =
        MutableStateFlow<UiState<GetCollegesByStateResponse>>(UiState.Empty)
    val getCollegesByStateState: StateFlow<UiState<GetCollegesByStateResponse>> =
        _getCollegesByStateState

    private val _getCollegeByIdState =
        MutableStateFlow<UiState<GetCollegeByIdResponse>>(UiState.Empty)
    val getCollegeByIdState: StateFlow<UiState<GetCollegeByIdResponse>> = _getCollegeByIdState

    private val _getCollegeCountState =
        MutableStateFlow<UiState<GetTotalCollegeCountResponse>>(UiState.Empty)
    val getCollegeCountState: StateFlow<UiState<GetTotalCollegeCountResponse>> =
        _getCollegeCountState

    private val _getPublicCollegesState =
        MutableStateFlow<UiState<PaginatedCollegesResponse>>(UiState.Empty)
    val getPublicCollegesState: StateFlow<UiState<PaginatedCollegesResponse>> =
        _getPublicCollegesState


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

    fun createCollege(request: CreateANewCollegeRequest) {
        executeWithUiState(_createCollegeState) {
            collegeRepository.createCollege(request)
        }
    }

    fun deleteCollege(collegeId: String) {
        executeWithUiState(_deleteCollegeState) {
            collegeRepository.deleteCollege(collegeId)
        }
    }

    fun updateCollege(collegeId: String, request: UpdateAnExistingCollegeRequest) {
        executeWithUiState(_updateCollegeState) {
            collegeRepository.updateCollege(collegeId, request)
        }
    }

    fun getAllCities() {
        executeWithUiState(_getAllCitiesState) {
            collegeRepository.getAllCities()
        }
    }

    fun getAllStates() {
        executeWithUiState(_getAllStatesState) {
            collegeRepository.getAllStates()
        }
    }

    fun getCollegesByCity(cityId: String) {
        executeWithUiState(_getCollegesByCityState) {
            collegeRepository.getCollegesByCity(cityId)
        }
    }

    fun getCollegesByState(stateId: String) {
        executeWithUiState(_getCollegesByStateState) {
            collegeRepository.getCollegeByState(stateId)
        }
    }

    fun getCollegeById(collegeId: String) {
        executeWithUiState(_getCollegeByIdState) {
            collegeRepository.getCollegeById(collegeId)
        }
    }

    fun getTotalCollegeCount() {
        executeWithUiState(_getCollegeCountState) {
            collegeRepository.getTotalCollegeCount()
        }
    }

    fun getPublicColleges(page: Int, size: Int) {
        executeWithUiState(_getPublicCollegesState) {
            collegeRepository.getPublicColleges(page, size)
        }
    }

    fun resetCollegeStates() {
        _createCollegeState.value = UiState.Empty
        _deleteCollegeState.value = UiState.Empty
        _updateCollegeState.value = UiState.Empty
        _getAllCitiesState.value = UiState.Empty
        _getAllStatesState.value = UiState.Empty
        _getCollegesByCityState.value = UiState.Empty
        _getCollegesByStateState.value = UiState.Empty
        _getCollegeByIdState.value = UiState.Empty
        _getCollegeCountState.value = UiState.Empty
        _getPublicCollegesState.value = UiState.Empty
    }
}
