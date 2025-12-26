package org.rajat.quickpick.domain.modal.ordermanagement
import kotlinx.serialization.Serializable

@Serializable
data class UpdateOrderStateRequest(
    val orderStatus: String?=null,
    val otp: String?=null
)