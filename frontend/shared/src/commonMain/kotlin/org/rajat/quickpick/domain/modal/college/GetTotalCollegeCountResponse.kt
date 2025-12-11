package org.rajat.quickpick.domain.modal.college

import kotlinx.serialization.Serializable

@Serializable
data class GetTotalCollegeCountResponse(
    val count: Int? = null
)