package org.rajat.quickpick.presentation.feature.myorders.components

import org.rajat.quickpick.domain.modal.ordermanagement.OrderStatus

sealed class OrderTab(val title: String, val statuses: List<OrderStatus>) {
    object Active : OrderTab("Active", listOf(
        OrderStatus.PENDING,
        OrderStatus.ACCEPTED,
        OrderStatus.PREPARING,
        OrderStatus.READY_FOR_PICKUP
    ))
    object Completed : OrderTab("Completed", listOf(OrderStatus.COMPLETED))
    object Cancelled : OrderTab("Cancelled", listOf(OrderStatus.CANCELLED, OrderStatus.REJECTED))
}