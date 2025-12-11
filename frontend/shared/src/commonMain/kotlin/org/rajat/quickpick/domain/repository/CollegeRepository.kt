package org.rajat.quickpick.domain.repository

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

interface CollegeRepository {

    suspend fun createCollege(collegeRequest: CreateANewCollegeRequest): Result<CreateANewCollegeResponse>
    suspend fun deleteCollege(collegeId: String): Result<DeleteACollegeResponse>
    suspend fun getAllCities(): Result<GetAllCitiesResponse>
    suspend fun getAllStates(): Result<GetAllStatesResponse>
    suspend fun getCollegesByCity(cityId: String): Result<GetCollegesByCityResponse>
    suspend fun getCollegeByState(stateId: String): Result<GetCollegesByStateResponse>
    suspend fun getCollegeById(collegeId: String): Result<GetCollegeByIdResponse>
    suspend fun getTotalCollegeCount(): Result<GetTotalCollegeCountResponse>
    suspend fun updateCollege(
        collegeId: String,
        updateAnExistingCollegeRequest: UpdateAnExistingCollegeRequest
    ): Result<UpdateAnExistingCollegeRequest>

    suspend fun getPublicColleges(page: Int, size: Int): Result<PaginatedCollegesResponse>
}