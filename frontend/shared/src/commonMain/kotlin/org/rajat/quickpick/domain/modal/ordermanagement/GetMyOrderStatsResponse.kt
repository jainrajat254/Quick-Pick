package org.rajat.quickpick.domain.modal.ordermanagement
import kotlinx.serialization.Serializable


@Serializable
data class GetMyOrderStatsResponse(
    val cancelledOrders: Int?=null,
    val completedOrders: Int?=null,
    val pendingOrders: Int?=null,
    val totalOrders: Int?=null
)