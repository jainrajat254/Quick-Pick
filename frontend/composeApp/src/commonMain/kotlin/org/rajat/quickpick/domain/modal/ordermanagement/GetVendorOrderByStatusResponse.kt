package org.rajat.quickpick.domain.modal.ordermanagement
import com.example.dummyproject.data.ordermanagement.getOrderById.GetOrderByIdResponse
import kotlinx.serialization.Serializable

@Serializable
class GetVendorOrderByStatusResponse : ArrayList<GetOrderByIdResponse>()