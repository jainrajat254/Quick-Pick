package org.rajat.quickpick.domain.repository

import org.rajat.quickpick.domain.modal.search.GetVendorByIDResponse
import org.rajat.quickpick.domain.modal.search.GetAllVendorsInCollegeResponse
import org.rajat.quickpick.domain.modal.search.SearchVendorsResponse
import org.rajat.quickpick.domain.modal.search.SearchMenuItemsResponse
import org.rajat.quickpick.domain.modal.search.StrictSearchMenuItemsResponse

interface SearchRepository {
    suspend fun searchVendors(query: String?): Result<SearchVendorsResponse>
    suspend fun getAllVendorsInCollege(): Result<GetAllVendorsInCollegeResponse>
    suspend fun getVendorById(vendorId: String): Result<GetVendorByIDResponse>
    suspend fun searchMenuItems(
        query: String? = null,
        vendorId: String? = null,
        category: String? = null,
        minPrice: Double? = null,
        maxPrice: Double? = null,
        availableOnly: Boolean = true,
        sortBy: String = "name",
        sortDirection: String = "ASC",
        page: Int = 0,
        size: Int = 20
    ): Result<SearchMenuItemsResponse>

    suspend fun strictSearchMenuItems(
        query: String? = null,
        vendorId: String? = null,
        category: String? = null,
        minPrice: Double? = null,
        maxPrice: Double? = null,
        availableOnly: Boolean = true,
        sortBy: String = "name",
        sortDirection: String = "ASC",
        page: Int = 0,
        size: Int = 20
    ): Result<StrictSearchMenuItemsResponse>
}