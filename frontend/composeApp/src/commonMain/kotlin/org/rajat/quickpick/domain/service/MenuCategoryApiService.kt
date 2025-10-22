package org.rajat.quickpick.domain.service

import org.rajat.quickpick.domain.modal.vendorMenuCategories.GetDefaultVendorCategoriesResponse
import org.rajat.quickpick.domain.modal.vendorMenuCategories.ResetVendorCategoriesToDefaultResponse
import org.rajat.quickpick.domain.modal.vendorMenuCategories.UpdateVendorCategoriesRequest
import org.rajat.quickpick.domain.modal.vendorMenuCategories.UpdateVendorCategoriesResponse

interface MenuCategoryApiService {

    suspend fun getDefaultVendorCategories(): GetDefaultVendorCategoriesResponse
    suspend fun resetVendorCategoriesToDefault(): ResetVendorCategoriesToDefaultResponse
    suspend fun updateVendorCategories(updateVendorCategoriesRequest: UpdateVendorCategoriesRequest): UpdateVendorCategoriesResponse
}