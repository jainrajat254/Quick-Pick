package org.rajat.quickpick.domain.modal.profile

import kotlinx.serialization.Serializable

@Serializable
data class UpdateVendorProfileResponse(
    val address: String?=null,
    val collegeName: String?=null,
    val email: String?=null,
    val emailVerified: Boolean?=null,
    val foodCategories: List<String?>?=null,
    val id: String?=null,
    val phone: String?=null,
    val phoneVerified: Boolean?=null,
    val profileImageUrl: String?=null,
    val role: String?=null,
    val storeName: String?=null,
    val vendorDescription: String?=null,
    val vendorName: String?=null
)