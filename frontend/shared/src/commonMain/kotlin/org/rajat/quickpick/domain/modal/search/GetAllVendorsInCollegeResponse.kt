package org.rajat.quickpick.domain.modal.search


import kotlinx.serialization.Serializable

@Serializable
data class GetAllVendorsInCollegeResponse(
    val count: Int,
    val vendors: List<Vendor>
)