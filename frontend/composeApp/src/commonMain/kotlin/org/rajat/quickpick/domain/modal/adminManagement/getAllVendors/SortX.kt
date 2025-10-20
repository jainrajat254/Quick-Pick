package org.rajat.quickpick.domain.modal.adminManagement.getAllVendors

import kotlinx.serialization.Serializable

@Serializable
data class SortX(
    val empty: Boolean,
    val sorted: Boolean,
    val unsorted: Boolean
)