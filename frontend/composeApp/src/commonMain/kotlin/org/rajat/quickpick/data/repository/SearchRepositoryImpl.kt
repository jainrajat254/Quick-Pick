package org.rajat.quickpick.data.repository

import org.rajat.quickpick.domain.modal.search.*
import org.rajat.quickpick.domain.repository.SearchRepository
import org.rajat.quickpick.domain.service.SearchApiService

class SearchRepositoryImpl(private val searchApiService: SearchApiService) : SearchRepository {
    override suspend fun searchVendors(query: String?): Result<SearchVendorsResponse> {
        return runCatching { searchApiService.searchVendors(query) }
    }

    override suspend fun getAllVendorsInCollege(): Result<GetAllVendorsInCollegeResponse> {
        return runCatching { searchApiService.getAllVendorsInCollege() }
    }

    override suspend fun getVendorById(vendorId: String): Result<GetVendorByIDResponse> {
        return runCatching { searchApiService.getVendorById(vendorId) }
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
    ): Result<SearchMenuItemsResponse> {
        return runCatching {
            searchApiService.searchMenuItems(
                query, vendorId, category, minPrice, maxPrice,
                availableOnly, sortBy, sortDirection, page, size
            )
        }
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
    ): Result<StrictSearchMenuItemsResponse> {
        return runCatching {
            searchApiService.strictSearchMenuItems(
                query, vendorId, category, minPrice, maxPrice,
                availableOnly, sortBy, sortDirection, page, size
            )
        }
    }
}