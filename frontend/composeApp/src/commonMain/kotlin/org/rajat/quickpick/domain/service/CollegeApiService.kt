package org.rajat.quickpick.domain.service

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

interface CollegeApiService {

    suspend fun createCollege(collegeRequest: CreateANewCollegeRequest): CreateANewCollegeResponse
    suspend fun deleteCollege(collegeId: String): DeleteACollegeResponse
    suspend fun getAllCities(): GetAllCitiesResponse
    suspend fun getAllStates(): GetAllStatesResponse
    suspend fun getCollegesByCity(cityId: String): GetCollegesByCityResponse
    suspend fun getCollegeByState(stateId: String): GetCollegesByStateResponse
    suspend fun getCollegeById(collegeId: String): GetCollegeByIdResponse
    suspend fun getTotalCollegeCount(): GetTotalCollegeCountResponse
    suspend fun updateCollege(
        collegeId: String,
        updateAnExistingCollegeRequest: UpdateAnExistingCollegeRequest
    ): UpdateAnExistingCollegeRequest

    suspend fun getPublicColleges(page: Int, size: Int): PaginatedCollegesResponse
}