package org.rajat.quickpick.domain.modal.college.paginatedColleges

import kotlinx.serialization.Serializable

@Serializable
data class Content(
    val address: String? = null,
    val city: String? = null,
    val id: String? = null,
    val name: String? = null,
    val state: String? = null
)