package org.rajat.quickpick.domain.modal.adminManagement

import kotlinx.serialization.Serializable

@Serializable
data class SuspendUserResponse(
    val collegeName: String? = null,
    val createdAt: String? = null,
    val department: String? = null,
    val email: String? = null,
    val emailVerified: Boolean? = null,
    val fullName: String? = null,
    val id: String? = null,
    val phone: String? = null,
    val phoneVerified: Boolean? = null,
    val role: String? = null,
    val suspended: Boolean? = null,
    val suspendedAt: String? = null,
    val suspensionReason: String? = null,
    val updatedAt: String? = null
)