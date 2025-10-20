package org.rajat.quickpick.domain.modal.menuitems

import kotlinx.serialization.Serializable

@Serializable
data class CreateMenuItemRequest(
    val category: String?=null,
    val description: String?=null,
    val imageUrl: String?=null,
    val isAvailable: Boolean?=null,
    val isVeg: Boolean?=null,
    val name: String?=null,
    val price: Double?=null,
    val quantity: Int?=null
)