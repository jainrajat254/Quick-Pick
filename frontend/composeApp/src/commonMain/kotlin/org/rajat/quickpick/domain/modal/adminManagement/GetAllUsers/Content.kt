package org.rajat.quickpick.domain.modal.adminManagement.getAllUsers

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class Content(
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
    @Contextual val suspendedAt: Any,
    @Contextual val suspensionReason: Any,
    val updatedAt: String
)