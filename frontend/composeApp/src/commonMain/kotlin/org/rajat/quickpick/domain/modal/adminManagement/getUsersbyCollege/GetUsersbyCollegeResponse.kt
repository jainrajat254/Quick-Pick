package org.rajat.quickpick.domain.modal.adminManagement.getUsersbyCollege

import kotlinx.serialization.Serializable

@Serializable
data class GetUsersbyCollegeResponse(
    val content: List<Any?>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: SortX,
    val totalElements: Int,
    val totalPages: Int
)