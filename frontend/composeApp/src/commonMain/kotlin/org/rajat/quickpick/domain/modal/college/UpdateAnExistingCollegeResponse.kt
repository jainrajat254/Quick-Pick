package org.rajat.quickpick.domain.modal.college

import kotlinx.serialization.Serializable

@Serializable
data class UpdateAnExistingCollegeResponse(
    val address: String,
    val city: String,
    val id: String,
    val name: String,
    val state: String
)