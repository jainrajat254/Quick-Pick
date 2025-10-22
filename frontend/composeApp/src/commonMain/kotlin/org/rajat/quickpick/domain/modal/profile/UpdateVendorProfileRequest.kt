package org.rajat.quickpick.domain.modal.profile

import kotlinx.serialization.Serializable

@Serializable
data class UpdateVendorProfileRequest(
    val address: String? = null,
    val foodCategories: List<String?>? = null,
    val phone: String? = null,
    val profileImageUrl: String? = null,
    val storeName: String? = null,
    val vendorDescription: String? = null,
    val vendorName: String? = null
)