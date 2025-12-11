package org.rajat.quickpick.domain.service

import org.rajat.quickpick.domain.modal.search.GetVendorByIDResponse
import org.rajat.quickpick.domain.modal.search.GetAllVendorsInCollegeResponse
import org.rajat.quickpick.domain.modal.search.SearchVendorsResponse
import org.rajat.quickpick.domain.modal.search.SearchMenuItemsResponse
import org.rajat.quickpick.domain.modal.search.StrictSearchMenuItemsResponse

interface SearchApiService {
    suspend fun searchVendors(query: String?): SearchVendorsResponse
    suspend fun getAllVendorsInCollege(): GetAllVendorsInCollegeResponse
    suspend fun getVendorById(vendorId: String): GetVendorByIDResponse
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
    ): SearchMenuItemsResponse

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
    ): StrictSearchMenuItemsResponse
}