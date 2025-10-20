package org.rajat.quickpick.domain.modal.adminManagement.getUsersbyCollege

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class GetUsersbyCollegeResponse(
    @Contextual val content: List<Any?>,
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