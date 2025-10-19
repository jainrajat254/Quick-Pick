package org.rajat.quickpick.domain.modal.admin

data class AdminCreateRequest(
    val email: String?,
    val fullName: String?,
    val password: String?
)