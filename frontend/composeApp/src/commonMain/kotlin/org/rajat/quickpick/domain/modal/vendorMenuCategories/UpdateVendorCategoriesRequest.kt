package org.rajat.quickpick.domain.modal.vendorMenuCategories

import kotlinx.serialization.Serializable

@Serializable
data class UpdateVendorCategoriesRequest(
    val categories: List<String?>?=null,
)