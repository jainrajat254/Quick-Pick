package org.rajat.quickpick.data.remote

import io.ktor.client.HttpClient
import org.rajat.quickpick.domain.modal.menuitems.*
import org.rajat.quickpick.domain.modal.menuitems.getMyMenuItemsPaginated.GetMyMenuItemsPaginatedResponse
import org.rajat.quickpick.domain.service.MenuItemApiService
import org.rajat.quickpick.utils.Constants
import org.rajat.quickpick.utils.network.*

class MenuItemApiServiceImpl(private val httpClient: HttpClient) : MenuItemApiService {

    override suspend fun createMenuItem(createMenuItemRequest: CreateMenuItemRequest): CreateMenuItemResponse {
        return httpClient.safePost(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.CREATE_MENU_ITEMS}",
            body = createMenuItemRequest
        )
    }

    override suspend fun getMyMenuItems(page: Int, size: Int): GetMyMenuItemsPaginatedResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_MY_VENUE_ITEM}",
            queryParams = mapOf("page" to page.toString(), "size" to size.toString())
        )
    }

    override suspend fun getMyAvailableMenuItems(): GetMyAvailableMenuItemsResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_MY_AVAILABLE_MENU_ITEMS}"
        )
    }

    override suspend fun getMyMenuItemsByCategory(category: String): GetMyMenuItemsByCategoryResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_MY_MENU_ITEMS_BY_CATEGORY}$category"
        )
    }

    override suspend fun searchMyMenuItems(query: String): SearchMyMenuItemsResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.SEARCH_MY_MENU_ITEMS}",
            queryParams = mapOf("query" to query)
        )
    }

    override suspend fun getMyMenuItemsByPriceRange(minPrice: Double, maxPrice: Double): GetMyMenuItemsByPriceRangeResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.MY_MENU_PRICE_RANGE}",
            queryParams = mapOf("minPrice" to minPrice.toString(), "maxPrice" to maxPrice.toString())
        )
    }

    override suspend fun getMyMenuCategories(): GetMyMenuCategoriesResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.MY_MENU_CATEGORIES}"
        )
    }

    override suspend fun updateMenuItem(menuItemId: String, updateMenuItemRequest: UpdateMenuItemRequest): UpdateMenuItemResponse {
        return httpClient.safePut(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.UPDATE_MENU_ITEMS}$menuItemId",
            body = updateMenuItemRequest
        )
    }

    override suspend fun toggleMenuItemAvailability(menuItemId: String): ToggleAvailabilityResponse {
        return httpClient.safePatch(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.UPDATE_MENU_ITEMS}$menuItemId/toggle-availability"
        )
    }

    override suspend fun updateMenuItemQuantity(menuItemId: String, quantity: Int): UpdateQuantityResponse {
        return httpClient.safePatch(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.UPDATE_MENU_ITEMS}$menuItemId/quantity",
            queryParams = mapOf("quantity" to quantity.toString())
        )
    }

    override suspend fun deleteMenuItem(menuItemId: String): DeleteMenuItemResponse {
        return httpClient.safeDelete(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.UPDATE_MENU_ITEMS}$menuItemId"
        )
    }

    override suspend fun deleteMultipleMenuItems(menuItemIds: List<String>): BulkDeleteMenuItemsResponse {
        return httpClient.safeDelete(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.MENU_ITEM_BULK_DELETE}",
            body = mapOf("menuItemIds" to menuItemIds)
        )
    }

    override suspend fun getMyMenuItemStats(): GetMyMenuItemStatsResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.MY_MENU_STATS}"
        )
    }

    override suspend fun getVendorMenu(vendorId: String): GetVendorMenuResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.VENDOR_MENU}$vendorId"
        )
    }

    override suspend fun getVendorMenuByCategory(vendorId: String, category: String): GetVendorMenuByCategoryResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.VENDOR_MENU}$vendorId/category/$category"
        )
    }

    override suspend fun getMenuItemById(menuItemId: String): CreateMenuItemResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.UPDATE_MENU_ITEMS}$menuItemId"
        )
    }
}