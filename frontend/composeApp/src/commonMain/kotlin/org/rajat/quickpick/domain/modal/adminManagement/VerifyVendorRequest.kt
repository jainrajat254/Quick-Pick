package org.rajat.quickpick.domain.modal.adminManagement

import kotlinx.serialization.Serializable

@Serializable
data class VerifyVendorRequest(
    val notes: String?=null
)