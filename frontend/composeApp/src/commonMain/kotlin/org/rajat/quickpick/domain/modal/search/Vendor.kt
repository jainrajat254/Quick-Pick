package org.rajat.quickpick.domain.modal.search

import kotlinx.serialization.Serializable

@Serializable
data class Vendor(
    val address: String,
    val collegeName: String,
    val email: String,
    val emailVerified: Boolean,
    val foodCategories: List<String>,
    val id: String,
    val phone: String,
    val phoneVerified: Boolean,
    val profileImageUrl: String,
    val role: String,
    val storeName: String,
    val vendorDescription: String,
    val vendorName: String
)