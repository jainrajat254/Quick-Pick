package org.rajat.quickpick.data.remote

import io.ktor.client.HttpClient
import org.rajat.quickpick.domain.modal.search.*
import org.rajat.quickpick.domain.service.SearchApiService
import org.rajat.quickpick.utils.Constants
import org.rajat.quickpick.utils.network.safeGet

class SearchApiServiceImpl(private val httpClient: HttpClient) : SearchApiService {
    override suspend fun searchVendors(query: String?): SearchVendorsResponse {
        val queryParams = if (!query.isNullOrBlank()) mapOf("query" to query) else emptyMap()
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.SEARCH_VENDORS}",
            queryParams = queryParams
        )
    }

    override suspend fun getAllVendorsInCollege(): GetAllVendorsInCollegeResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.SEARCH_VENDORS_COLLEGE}"
        )
    }

    override suspend fun getVendorById(vendorId: String): GetVendorByIDResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.SEARCH_VENDOR_BY_ID + vendorId}"
        )
    }

    override suspend fun searchMenuItems(
        query: String?,
        vendorId: String?,
        category: String?,
        minPrice: Double?,
        maxPrice: Double?,
        availableOnly: Boolean,
        sortBy: String,
        sortDirection: String,
        page: Int,
        size: Int
    ): SearchMenuItemsResponse {
        val queryParams = buildMap {
            query?.let { put("query", it) }
            vendorId?.let { put("vendorId", it) }
            category?.let { put("category", it) }
            minPrice?.let { put("minPrice", it.toString()) }
            maxPrice?.let { put("maxPrice", it.toString()) }
            put("availableOnly", availableOnly.toString())
            put("sortBy", sortBy)
            put("sortDirection", sortDirection)
            put("page", page.toString())
            put("size", size.toString())
        }
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.SEARCH_MENU_ITEMS}",
            queryParams = queryParams
        )
    }

    override suspend fun strictSearchMenuItems(
        query: String?,
        vendorId: String?,
        category: String?,
        minPrice: Double?,
        maxPrice: Double?,
        availableOnly: Boolean,
        sortBy: String,
        sortDirection: String,
        page: Int,
        size: Int
    ): StrictSearchMenuItemsResponse {
        val queryParams = buildMap {
            query?.let { put("query", it) }
            vendorId?.let { put("vendorId", it) }
            category?.let { put("category", it) }
            minPrice?.let { put("minPrice", it.toString()) }
            maxPrice?.let { put("maxPrice", it.toString()) }
            put("availableOnly", availableOnly.toString())
            put("sortBy", sortBy)
            put("sortDirection", sortDirection)
            put("page", page.toString())
            put("size", size.toString())
        }
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.STRICT_SEARCH_MENU_ITEMS}",
            queryParams = queryParams
        )
    }
}