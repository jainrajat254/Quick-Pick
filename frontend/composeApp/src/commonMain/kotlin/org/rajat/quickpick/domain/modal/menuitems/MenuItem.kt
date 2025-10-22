package org.rajat.quickpick.domain.modal.menuitems

import kotlinx.serialization.Serializable

@Serializable
data class MenuItem(
    val available: Boolean? = null,
    val category: String? = null,
    val createdAt: String? = null,
    val description: String? = null,
    val id: String? = null,
    val imageUrl: String? = null,
    val name: String? = null,
    val price: Double,
    val quantity: Int? = null,
    val updatedAt: String? = null,
    val veg: Boolean? = null,
    val vendorId: String? = null
)