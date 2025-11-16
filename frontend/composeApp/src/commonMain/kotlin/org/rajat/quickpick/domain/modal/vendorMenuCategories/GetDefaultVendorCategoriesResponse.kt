package org.rajat.quickpick.domain.modal.vendorMenuCategories


import kotlinx.serialization.Serializable

@Serializable
data class GetDefaultVendorCategoriesResponse(
    val categories: List<String>,
    val count: Int
)
