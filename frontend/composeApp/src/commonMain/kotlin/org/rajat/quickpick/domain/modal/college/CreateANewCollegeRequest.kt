package org.rajat.quickpick.domain.modal.college

import kotlinx.serialization.Serializable

@Serializable
data class CreateANewCollegeRequest(
    val address: String? = null,
    val city: String? = null,
    val name: String? = null,
    val state: String? = null
)