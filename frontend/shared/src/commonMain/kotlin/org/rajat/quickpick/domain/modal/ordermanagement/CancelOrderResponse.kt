package org.rajat.quickpick.domain.modal.ordermanagement
import kotlinx.serialization.Serializable

@Serializable
data class CancelOrderResponse(
    val message: String?=null
)