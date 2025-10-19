package org.rajat.quickpick.domain.modal.college.paginatedColleges

import kotlinx.serialization.Serializable

@Serializable
data class Content(
    val address: String,
    val city: String,
    val id: String,
    val name: String,
    val state: String
)