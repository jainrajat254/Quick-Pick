package org.rajat.quickpick.domain.modal.menuitems

import kotlinx.serialization.Serializable

@Serializable
data class BulkDeleteMenuItemsRequest(
    val menuItemIds: List<String?>?=null
)