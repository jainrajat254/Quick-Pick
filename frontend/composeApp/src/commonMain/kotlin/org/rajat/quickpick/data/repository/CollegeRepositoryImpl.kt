package org.rajat.quickpick.data.repository

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
import org.rajat.quickpick.domain.service.CollegeApiService

class CollegeRepositoryImpl(private val collegeApiService: CollegeApiService) : CollegeRepository {

    override suspend fun createCollege(collegeRequest: CreateANewCollegeRequest): Result<CreateANewCollegeResponse> {
        return runCatching {
            collegeApiService.createCollege(collegeRequest = collegeRequest)
        }
    }

    override suspend fun deleteCollege(collegeId: String): Result<DeleteACollegeResponse> {
        return runCatching {
            collegeApiService.deleteCollege(collegeId = collegeId)
        }
    }

    override suspend fun getAllCities(): Result<GetAllCitiesResponse> {
        return runCatching {
            collegeApiService.getAllCities()
        }
    }

    override suspend fun getAllStates(): Result<GetAllStatesResponse> {
        return runCatching {
            collegeApiService.getAllStates()
        }
    }

    override suspend fun getCollegesByCity(cityId: String): Result<GetCollegesByCityResponse> {
        return runCatching {
            collegeApiService.getCollegesByCity(cityId = cityId)
        }
    }

    override suspend fun getCollegeByState(stateId: String): Result<GetCollegesByStateResponse> {
        return runCatching {
            collegeApiService.getCollegeByState(stateId = stateId)
        }
    }

    override suspend fun getCollegeById(collegeId: String): Result<GetCollegeByIdResponse> {
        return runCatching {
            collegeApiService.getCollegeById(collegeId = collegeId)
        }
    }

    override suspend fun getTotalCollegeCount(): Result<GetTotalCollegeCountResponse> {
        return runCatching {
            collegeApiService.getTotalCollegeCount()
        }
    }

    override suspend fun updateCollege(
        collegeId: String,
        updateAnExistingCollegeRequest: UpdateAnExistingCollegeRequest
    ): Result<UpdateAnExistingCollegeRequest> {
        return runCatching {
            collegeApiService.updateCollege(
                collegeId = collegeId,
                updateAnExistingCollegeRequest = updateAnExistingCollegeRequest
            )
        }
    }

    override suspend fun getPublicColleges(
        page: Int,
        size: Int
    ): Result<PaginatedCollegesResponse> {
        return runCatching {
            collegeApiService.getPublicColleges(page = page, size = size)
        }
    }
}