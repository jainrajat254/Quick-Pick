package org.rajat.quickpick.domain.modal.adminManagement

import kotlinx.serialization.Serializable

@Serializable
data class UnsuspendVendorResponse(
    val address: String? = null,
    val adminNotes: String? = null,
    val collegeName: String? = null,
    val createdAt: String? = null,
    val email: String? = null,
    val emailVerified: Boolean? = null,
    val foodCategories: List<String?>? = null,
    val gstNumber: String? = null,
    val id: String? = null,
    val licenseNumber: String? = null,
    val phone: String? = null,
    val phoneVerified: Boolean? = null,
    val rejectedAt: String? = null,
    val rejectionReason: String? = null,
    val role: String? = null,
    val storeName: String? = null,
    val suspended: Boolean? = null,
    val suspendedAt: String? = null,
    val suspensionReason: String? = null,
    val updatedAt: String? = null,
    val vendorDescription: String? = null,
    val vendorName: String? = null,
    val verificationStatus: String? = null,
    val verifiedAt: String? = null
)