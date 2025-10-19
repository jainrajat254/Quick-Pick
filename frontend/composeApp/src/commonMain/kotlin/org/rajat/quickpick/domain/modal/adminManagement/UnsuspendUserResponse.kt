package org.rajat.quickpick.domain.modal.adminManagement

import kotlinx.serialization.Serializable

@Serializable
data class UnsuspendUserResponse(
    val collegeName: String,
    val createdAt: String,
    val department: String,
    val email: String,
    val emailVerified: Boolean,
    val fullName: String,
    val id: String,
    val phone: String,
    val phoneVerified: Boolean,
    val role: String,
    val studentId: String,
    val suspended: Boolean,
    val suspendedAt: Any,
    val suspensionReason: Any,
    val updatedAt: String
)