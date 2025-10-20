package org.rajat.quickpick.domain.modal.college

import kotlinx.serialization.Serializable

@Serializable
data class UpdateAnExistingCollegeRequest(
    val address: String?,
    val city: String?,
    val name: String?,
    val state: String?
)