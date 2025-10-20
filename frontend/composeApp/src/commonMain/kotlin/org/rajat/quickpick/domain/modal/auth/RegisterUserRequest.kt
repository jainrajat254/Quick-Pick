package org.rajat.quickpick.domain.modal.auth

import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserRequest(
    val collegeName: String?=null,
    val department: String?=null,
    val email: String?=null,
    val fullName: String?=null,
    val gender: String?=null,
    val password: String?=null,
    val phone: String?=null,
    val studentId: String?=null
)