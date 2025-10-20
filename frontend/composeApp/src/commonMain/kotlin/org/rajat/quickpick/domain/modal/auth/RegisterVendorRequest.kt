package org.rajat.quickpick.domain.modal.auth

import kotlinx.serialization.Serializable

@Serializable
data class RegisterVendorRequest(
    val address: String?=null,
    val collegeName: String?=null,
    val email: String?=null,
    val foodCategories: List<String?>?=null,
    val foodLicenseNumber: String?=null,
    val gstNumber: String?=null,
    val licenseNumber: String?=null,
    val password: String?=null,
    val phone: String?=null,
    val storeName: String?=null,
    val vendorDescription: String?=null,
    val vendorName: String?=null
)