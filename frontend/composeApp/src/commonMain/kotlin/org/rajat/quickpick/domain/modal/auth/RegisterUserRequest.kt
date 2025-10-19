package org.rajat.quickpick.domain.modal.auth

data class RegisterUserRequest(
    val collegeName: String?,
    val department: String?,
    val email: String?,
    val fullName: String?,
    val gender: String?,
    val password: String?,
    val phone: String?,
    val studentId: String?
)