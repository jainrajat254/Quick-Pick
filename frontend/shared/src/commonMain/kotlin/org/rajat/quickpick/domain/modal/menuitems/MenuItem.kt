package org.rajat.quickpick.domain.modal.menuitems

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class MenuItem(
    val available: Boolean? = null,
    val category: String? = null,
    val createdAt: String? = null,
    val description: String? = null,
    val id: String? = null,
    val imageUrl: String? = null,
    val name: String? = null,
    val price: Double? = null,
    val quantity: Int? = null,
    val updatedAt: String? = null,
    @SerialName("isVeg")
    @JsonNames("veg")
    val isVeg: Boolean? = null,
    val vendorId: String? = null
)