package org.rajat.quickpick.domain.repository

import org.rajat.quickpick.domain.modal.vendorMenuCategories.GetDefaultVendorCategoriesResponse
import org.rajat.quickpick.domain.modal.vendorMenuCategories.ResetVendorCategoriesToDefaultResponse
import org.rajat.quickpick.domain.modal.vendorMenuCategories.UpdateVendorCategoriesRequest
import org.rajat.quickpick.domain.modal.vendorMenuCategories.UpdateVendorCategoriesResponse

interface MenuCategoryRepository {
    suspend fun getDefaultVendorCategories(): Result<GetDefaultVendorCategoriesResponse>
    suspend fun resetVendorCategoriesToDefault(): Result<ResetVendorCategoriesToDefaultResponse>
    suspend fun updateVendorCategories(updateVendorCategoriesRequest: UpdateVendorCategoriesRequest): Result<UpdateVendorCategoriesResponse>
}