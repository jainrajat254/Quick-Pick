package org.rajat.quickpick.data.remote

import io.ktor.client.HttpClient
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
import org.rajat.quickpick.domain.service.CollegeApiService
import org.rajat.quickpick.utils.Constants
import org.rajat.quickpick.utils.network.safeDelete
import org.rajat.quickpick.utils.network.safeGet
import org.rajat.quickpick.utils.network.safePost
import org.rajat.quickpick.utils.network.safePut

class CollegeApiServiceImpl(private val httpClient: HttpClient) : CollegeApiService {

    override suspend fun createCollege(collegeRequest: CreateANewCollegeRequest): CreateANewCollegeResponse {
        return httpClient.safePost(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.CREATE_COLLEGE}",
            body = collegeRequest
        )
    }

    override suspend fun deleteCollege(collegeId: String): DeleteACollegeResponse {
        return httpClient.safeDelete(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.DELETE_COLLEGE}$collegeId"
        )
    }

    override suspend fun getAllCities(): GetAllCitiesResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_ALL_CITIES}"
        )
    }

    override suspend fun getAllStates(): GetAllStatesResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_ALL_STATES}"
        )
    }

    override suspend fun getCollegesByCity(cityId: String): GetCollegesByCityResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_COLLEGES_BY_CITY}$cityId"
        )
    }

    override suspend fun getCollegeByState(stateId: String): GetCollegesByStateResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_COLLEGES_BY_STATE}$stateId"
        )
    }

    override suspend fun getCollegeById(collegeId: String): GetCollegeByIdResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_COLLEGE_BY_ID}$collegeId"
        )
    }

    override suspend fun getTotalCollegeCount(): GetTotalCollegeCountResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_TOTAL_COLLEGE_COUNT}"
        )
    }

    override suspend fun updateCollege(
        collegeId: String,
        updateAnExistingCollegeRequest: UpdateAnExistingCollegeRequest
    ): UpdateAnExistingCollegeRequest {
        return httpClient.safePut(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.UPDATE_COLLEGE}$collegeId",
            body = updateAnExistingCollegeRequest
        )
    }

    override suspend fun getPublicColleges(page: Int, size: Int): PaginatedCollegesResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_PUBLIC_COLLEGES}",
            queryParams = mapOf("page" to page.toString(), "size" to size.toString())
        )
    }
}
