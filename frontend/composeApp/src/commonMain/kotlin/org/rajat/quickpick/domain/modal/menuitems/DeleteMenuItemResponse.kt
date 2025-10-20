package org.rajat.quickpick.domain.modal.menuitems
import kotlinx.serialization.Serializable

@Serializable
data class DeleteMenuItemResponse(
    val message: String?=null
)