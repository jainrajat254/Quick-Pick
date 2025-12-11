package org.rajat.quickpick.domain.modal.adminManagement

import kotlinx.serialization.Serializable

@Serializable
data class SuspendVendorRequest(
    val reason: String? = null
)