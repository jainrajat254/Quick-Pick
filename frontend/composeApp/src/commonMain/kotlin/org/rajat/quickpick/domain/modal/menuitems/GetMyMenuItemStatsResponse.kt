package org.rajat.quickpick.domain.modal.menuitems
import kotlinx.serialization.Serializable

@Serializable
data class GetMyMenuItemStatsResponse(
    val availableItems: Int?=null,
    val totalItems: Int?=null,
    val unavailableItems: Int?=null
)