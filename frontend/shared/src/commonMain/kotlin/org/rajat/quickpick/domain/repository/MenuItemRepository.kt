package org.rajat.quickpick.domain.repository


import org.rajat.quickpick.domain.modal.menuitems.*
import org.rajat.quickpick.domain.modal.menuitems.getMyMenuItemsPaginated.GetMyMenuItemsPaginatedResponse

interface MenuItemRepository {

    suspend fun createMenuItem(createMenuItemRequest: CreateMenuItemRequest): Result<CreateMenuItemResponse>
    suspend fun getMyMenuItems(page: Int, size: Int): Result<GetMyMenuItemsPaginatedResponse>
    suspend fun getMyAvailableMenuItems(): Result<GetMyAvailableMenuItemsResponse>
    suspend fun getMyMenuItemsByCategory(category: String): Result<GetMyMenuItemsByCategoryResponse>
    suspend fun searchMyMenuItems(query: String): Result<SearchMyMenuItemsResponse>
    suspend fun getMyMenuItemsByPriceRange(minPrice: Double, maxPrice: Double): Result<GetMyMenuItemsByPriceRangeResponse>
    suspend fun getMyMenuCategories(): Result<GetMyMenuCategoriesResponse>
    suspend fun updateMenuItem(menuItemId: String, updateMenuItemRequest: UpdateMenuItemRequest): Result<UpdateMenuItemResponse>
    suspend fun toggleMenuItemAvailability(menuItemId: String): Result<ToggleAvailabilityResponse>
    suspend fun updateMenuItemQuantity(menuItemId: String, quantity: Int): Result<UpdateQuantityResponse>
    suspend fun deleteMenuItem(menuItemId: String): Result<DeleteMenuItemResponse>
    suspend fun deleteMultipleMenuItems(menuItemIds: List<String>): Result<BulkDeleteMenuItemsResponse>
    suspend fun getMyMenuItemStats(): Result<GetMyMenuItemStatsResponse>
    suspend fun getVendorMenu(vendorId: String): Result<GetVendorMenuResponse>
    suspend fun getVendorMenuByCategory(vendorId: String, category: String): Result<GetVendorMenuByCategoryResponse>
    suspend fun getMenuItemById(menuItemId: String): Result<CreateMenuItemResponse>
}