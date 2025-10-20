package org.rajat.quickpick.domain.modal.adminManagement.getAllUsers

import kotlinx.serialization.Serializable

@Serializable
data class SortX(
    val empty: Boolean ?=null,
    val sorted: Boolean ?=null,
    val unsorted: Boolean?=null
)