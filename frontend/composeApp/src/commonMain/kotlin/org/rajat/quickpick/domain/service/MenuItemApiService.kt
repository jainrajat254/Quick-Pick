package org.rajat.quickpick.domain.service


import org.rajat.quickpick.domain.modal.menuitems.BulkDeleteMenuItemsResponse
import org.rajat.quickpick.domain.modal.menuitems.CreateMenuItemRequest
import org.rajat.quickpick.domain.modal.menuitems.CreateMenuItemResponse
import org.rajat.quickpick.domain.modal.menuitems.UpdateMenuItemRequest
import org.rajat.quickpick.domain.modal.menuitems.UpdateMenuItemResponse
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
import org.rajat.quickpick.domain.modal.menuitems.UpdateQuantityResponse
import org.rajat.quickpick.domain.modal.menuitems.getMyMenuItemsPaginated.GetMyMenuItemsPaginatedResponse

interface MenuItemApiService {

    suspend fun createMenuItem(createMenuItemRequest: CreateMenuItemRequest): CreateMenuItemResponse
    suspend fun getMyMenuItems(page: Int, size: Int): GetMyMenuItemsPaginatedResponse
    suspend fun getMyAvailableMenuItems(): GetMyAvailableMenuItemsResponse
    suspend fun getMyMenuItemsByCategory(category: String): GetMyMenuItemsByCategoryResponse
    suspend fun searchMyMenuItems(query: String): SearchMyMenuItemsResponse
    suspend fun getMyMenuItemsByPriceRange(minPrice: Double, maxPrice: Double): GetMyMenuItemsByPriceRangeResponse
    suspend fun getMyMenuCategories(): GetMyMenuCategoriesResponse
    suspend fun updateMenuItem(menuItemId: String, updateMenuItemRequest: UpdateMenuItemRequest): UpdateMenuItemResponse
    suspend fun toggleMenuItemAvailability(menuItemId: String): ToggleAvailabilityResponse
    suspend fun updateMenuItemQuantity(menuItemId: String, quantity: Int): UpdateQuantityResponse
    suspend fun deleteMenuItem(menuItemId: String): DeleteMenuItemResponse
    suspend fun deleteMultipleMenuItems(menuItemIds: List<String>): BulkDeleteMenuItemsResponse
    suspend fun getMyMenuItemStats(): GetMyMenuItemStatsResponse
    suspend fun getVendorMenu(vendorId: String): GetVendorMenuResponse
    suspend fun getVendorMenuByCategory(vendorId: String, category: String): GetVendorMenuByCategoryResponse
    suspend fun getMenuItemById(menuItemId: String): CreateMenuItemResponse
}