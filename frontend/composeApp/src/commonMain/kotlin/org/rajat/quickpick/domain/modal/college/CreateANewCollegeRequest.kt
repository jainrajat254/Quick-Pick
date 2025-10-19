package org.rajat.quickpick.domain.modal.college

import kotlinx.serialization.Serializable

@Serializable
data class CreateANewCollegeRequest(
    val address: String?,
    val city: String?,
    val name: String?,
    val state: String?
)