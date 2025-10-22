package org.rajat.quickpick.data.repository

import org.rajat.quickpick.domain.modal.vendorMenuCategories.GetDefaultVendorCategoriesResponse
import org.rajat.quickpick.domain.modal.vendorMenuCategories.ResetVendorCategoriesToDefaultResponse
import org.rajat.quickpick.domain.modal.vendorMenuCategories.UpdateVendorCategoriesRequest
import org.rajat.quickpick.domain.modal.vendorMenuCategories.UpdateVendorCategoriesResponse
import org.rajat.quickpick.domain.repository.MenuCategoryRepository
import org.rajat.quickpick.domain.service.MenuCategoryApiService

class MenuCategoryRepositoryImpl(private val menuCategoryApiService: MenuCategoryApiService) :
    MenuCategoryRepository {
    override suspend fun getDefaultVendorCategories(): Result<GetDefaultVendorCategoriesResponse> {
        return runCatching {
            menuCategoryApiService.getDefaultVendorCategories()
        }
    }

    override suspend fun resetVendorCategoriesToDefault(): Result<ResetVendorCategoriesToDefaultResponse> {
        return runCatching {
            menuCategoryApiService.resetVendorCategoriesToDefault()
        }
    }

    override suspend fun updateVendorCategories(updateVendorCategoriesRequest: UpdateVendorCategoriesRequest): Result<UpdateVendorCategoriesResponse> {
        return runCatching {
            menuCategoryApiService.updateVendorCategories(updateVendorCategoriesRequest = updateVendorCategoriesRequest)
        }
    }
}