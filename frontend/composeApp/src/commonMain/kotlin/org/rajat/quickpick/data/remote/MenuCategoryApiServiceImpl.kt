package org.rajat.quickpick.data.remote

import io.ktor.client.HttpClient
import org.rajat.quickpick.domain.modal.vendorMenuCategories.GetDefaultVendorCategoriesResponse
import org.rajat.quickpick.domain.modal.vendorMenuCategories.ResetVendorCategoriesToDefaultResponse
import org.rajat.quickpick.domain.modal.vendorMenuCategories.UpdateVendorCategoriesRequest
import org.rajat.quickpick.domain.modal.vendorMenuCategories.UpdateVendorCategoriesResponse
import org.rajat.quickpick.domain.service.MenuCategoryApiService
import org.rajat.quickpick.utils.Constants
import org.rajat.quickpick.utils.network.safeGet
import org.rajat.quickpick.utils.network.safePut

class MenuCategoryApiServiceImpl(private val httpClient: HttpClient) : MenuCategoryApiService {

    override suspend fun getDefaultVendorCategories(): GetDefaultVendorCategoriesResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_DEFAULT_VENDOR_CATEGORIES}",
        )
    }

    override suspend fun resetVendorCategoriesToDefault(): ResetVendorCategoriesToDefaultResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.RESET_VENDOR_CATEGORIES_TO_DEFAULT}",
        )
    }

    override suspend fun updateVendorCategories(updateVendorCategoriesRequest: UpdateVendorCategoriesRequest): UpdateVendorCategoriesResponse {
        return httpClient.safePut(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.UPDATE_VENDOR_CATEGORIES}",
            body = updateVendorCategoriesRequest
        )
    }
}