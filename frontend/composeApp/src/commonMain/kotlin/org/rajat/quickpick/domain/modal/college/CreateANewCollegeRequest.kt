package org.rajat.quickpick.domain.modal.college

data class CreateANewCollegeRequest(
    val address: String?,
    val city: String?,
    val name: String?,
    val state: String?
)