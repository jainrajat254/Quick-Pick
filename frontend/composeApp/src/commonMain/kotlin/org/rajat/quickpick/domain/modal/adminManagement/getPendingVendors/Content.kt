package org.rajat.quickpick.domain.modal.adminManagement.getPendingVendors

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class Content(
    val address: String,
    val adminNotes: String?,
    val collegeName: String,
    val createdAt: String,
    val email: String,
    val emailVerified: Boolean,
    val foodCategories: List<String>,
    val foodLicenseNumber: String,
    val gstNumber: String,
    val id: String,
    val licenseNumber: String,
    val phone: String,
    val phoneVerified: Boolean,
    val rejectedAt: String?,
    val rejectionReason: String?,
    val role: String,
    val storeName: String,
    val suspended: Boolean,
    val suspendedAt: String?,
    val suspensionReason: String?,
    val updatedAt: String,
    val vendorDescription: String,
    val vendorName: String,
    val verificationStatus: String,
    val verifiedAt: String?
)