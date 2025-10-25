package org.rajat.quickpick.presentation.feature.myorders
import org.rajat.quickpick.domain.modal.ordermanagement.getOrderById.GetOrderByIdResponse
import org.rajat.quickpick.domain.modal.ordermanagement.getOrderById.OrderItemX

val dummyActiveOrders = listOf(
    GetOrderByIdResponse(
        id = "1", storeName = "Campus Cafe", totalAmount = 12.50,
        createdAt = "2025-10-23T10:15:00Z", orderStatus = "PENDING",
        orderItems = listOf(OrderItemX(menuItemName = "Coffee Latte", quantity = 1),
            OrderItemX(menuItemName = "Coffee Latte", quantity = 1))
    ),
    GetOrderByIdResponse(
        id = "2", storeName = "Hostel Kitchen B", totalAmount = 25.00,
        createdAt = "2025-10-23T09:30:00Z", orderStatus = "ACCEPTED",
        orderItems = listOf(
            OrderItemX(menuItemName = "Veggie Wrap", quantity = 1),
            OrderItemX(menuItemName = "Orange Juice", quantity = 1)
        )
    ),
    GetOrderByIdResponse(
        id = "3", storeName = "Pizza Place", totalAmount = 35.75,
        createdAt = "2025-10-22T18:00:00Z", orderStatus = "READY_FOR_PICKUP",
        orderItems = listOf(OrderItemX(menuItemName = "Pepperoni Pizza", quantity = 1))
    )
)
val dummyCompletedOrders = listOf(
    GetOrderByIdResponse(
        id = "4", storeName = "Canteen Central", totalAmount = 55.00,
        createdAt = "2025-10-21T13:00:00Z", orderStatus = "COMPLETED",
        orderItems = listOf(
            OrderItemX(menuItemName = "Chicken Curry Meal", quantity = 1),
            OrderItemX(menuItemName = "Rice", quantity = 1)
        )
    ),
    GetOrderByIdResponse(
        id = "5", storeName = "Burger Spot", totalAmount = 18.25,
        createdAt = "2025-10-20T19:45:00Z", orderStatus = "COMPLETED",
        orderItems = listOf(
            OrderItemX(menuItemName = "Bean and Veggie Burger", quantity = 1),
            OrderItemX(menuItemName = "Fries", quantity = 1)
        )
    )
)
val dummyCancelledOrders = listOf(
    GetOrderByIdResponse(
        id = "6", storeName = "Sweet Treats", totalAmount = 8.50,
        createdAt = "2025-10-19T11:00:00Z", orderStatus = "CANCELLED",
        orderItems = listOf(OrderItemX(menuItemName = "Strawberry Cheesecake", quantity = 1))
    )
)
val allOrders = dummyActiveOrders + dummyCompletedOrders + dummyCancelledOrders


