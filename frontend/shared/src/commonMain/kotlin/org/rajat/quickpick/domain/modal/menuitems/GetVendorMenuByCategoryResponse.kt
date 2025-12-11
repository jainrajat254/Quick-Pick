package org.rajat.quickpick.domain.modal.menuitems

import kotlinx.serialization.Serializable

@Serializable
data class GetVendorMenuByCategoryResponse(
    val menuItems: List<CreateMenuItemResponse>? = null,
    val count: Int? = null
)
