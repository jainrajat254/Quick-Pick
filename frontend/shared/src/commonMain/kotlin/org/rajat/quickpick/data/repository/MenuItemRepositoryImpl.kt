package org.rajat.quickpick.data.repository

import org.rajat.quickpick.domain.service.MenuItemApiService
import org.rajat.quickpick.domain.modal.menuitems.BulkDeleteMenuItemsResponse
import org.rajat.quickpick.domain.modal.menuitems.CreateMenuItemRequest
import org.rajat.quickpick.domain.modal.menuitems.CreateMenuItemResponse
import org.rajat.quickpick.domain.modal.menuitems.DeleteMenuItemResponse
import org.rajat.quickpick.domain.modal.menuitems.GetMyAvailableMenuItemsResponse
import org.rajat.quickpick.domain.modal.menuitems.GetMyMenuCategoriesResponse
import org.rajat.quickpick.domain.modal.menuitems.GetMyMenuItemStatsResponse
import org.rajat.quickpick.domain.modal.menuitems.GetMyMenuItemsByCategoryResponse
import org.rajat.quickpick.domain.modal.menuitems.GetMyMenuItemsByPriceRangeResponse
import org.rajat.quickpick.domain.modal.menuitems.GetVendorMenuByCategoryResponse
import org.rajat.quickpick.domain.modal.menuitems.GetVendorMenuResponse
import org.rajat.quickpick.domain.modal.menuitems.SearchMyMenuItemsResponse
import org.rajat.quickpick.domain.modal.menuitems.ToggleAvailabilityResponse
import org.rajat.quickpick.domain.modal.menuitems.UpdateMenuItemRequest
import org.rajat.quickpick.domain.modal.menuitems.UpdateMenuItemResponse
import org.rajat.quickpick.domain.modal.menuitems.UpdateQuantityResponse
import org.rajat.quickpick.domain.modal.menuitems.getMyMenuItemsPaginated.GetMyMenuItemsPaginatedResponse
import org.rajat.quickpick.domain.repository.MenuItemRepository

class MenuItemRepositoryImpl(private val menuItemApiService: MenuItemApiService): MenuItemRepository {
    override suspend fun createMenuItem(createMenuItemRequest: CreateMenuItemRequest): Result<CreateMenuItemResponse> {
        return runCatching {
            menuItemApiService.createMenuItem(createMenuItemRequest)
        }
    }

    override suspend fun getMyMenuItems(
        page: Int,
        size: Int
    ): Result<GetMyMenuItemsPaginatedResponse> {
        return runCatching {
            menuItemApiService.getMyMenuItems(page, size)
        }
    }

    override suspend fun getMyAvailableMenuItems(): Result<GetMyAvailableMenuItemsResponse> {
        return runCatching {
            menuItemApiService.getMyAvailableMenuItems()
        }
    }

    override suspend fun getMyMenuItemsByCategory(category: String): Result<GetMyMenuItemsByCategoryResponse> {
        return runCatching {
            menuItemApiService.getMyMenuItemsByCategory(category)
        }
    }

    override suspend fun searchMyMenuItems(query: String): Result<SearchMyMenuItemsResponse> {
        return runCatching {
            menuItemApiService.searchMyMenuItems(query)
        }
    }

    override suspend fun getMyMenuItemsByPriceRange(
        minPrice: Double,
        maxPrice: Double
    ): Result<GetMyMenuItemsByPriceRangeResponse> {
        return runCatching {
            menuItemApiService.getMyMenuItemsByPriceRange(minPrice, maxPrice)
        }
    }

    override suspend fun getMyMenuCategories(): Result<GetMyMenuCategoriesResponse> {
        return runCatching {
            menuItemApiService.getMyMenuCategories()
        }
    }

    override suspend fun updateMenuItem(
        menuItemId: String,
        updateMenuItemRequest: UpdateMenuItemRequest
    ): Result<UpdateMenuItemResponse> {
        return runCatching {
            menuItemApiService.updateMenuItem(menuItemId, updateMenuItemRequest)
        }
    }

    override suspend fun toggleMenuItemAvailability(menuItemId: String): Result<ToggleAvailabilityResponse> {
        return runCatching {
            menuItemApiService.toggleMenuItemAvailability(menuItemId)
        }
    }

    override suspend fun updateMenuItemQuantity(
        menuItemId: String,
        quantity: Int
    ): Result<UpdateQuantityResponse> {
        return runCatching {
            menuItemApiService.updateMenuItemQuantity(menuItemId, quantity)
        }
    }

    override suspend fun deleteMenuItem(menuItemId: String): Result<DeleteMenuItemResponse> {
        return runCatching {
            menuItemApiService.deleteMenuItem(menuItemId)
        }
    }

    override suspend fun deleteMultipleMenuItems(menuItemIds: List<String>): Result<BulkDeleteMenuItemsResponse> {
        return runCatching {
            menuItemApiService.deleteMultipleMenuItems(menuItemIds)
        }
    }

    override suspend fun getMyMenuItemStats(): Result<GetMyMenuItemStatsResponse> {
        return runCatching {
            menuItemApiService.getMyMenuItemStats()
        }
    }

    override suspend fun getVendorMenu(vendorId: String): Result<GetVendorMenuResponse> {
        return runCatching {
            menuItemApiService.getVendorMenu(vendorId)
        }
    }

    override suspend fun getVendorMenuByCategory(
        vendorId: String,
        category: String
    ): Result<GetVendorMenuByCategoryResponse> {
        return runCatching {
            menuItemApiService.getVendorMenuByCategory(vendorId, category)
        }
    }

    override suspend fun getMenuItemById(menuItemId: String): Result<CreateMenuItemResponse> {
        return runCatching {
            menuItemApiService.getMenuItemById(menuItemId)
        }
    }


}