package org.rajat.quickpick.domain.modal.college.paginatedColleges

import kotlinx.serialization.Serializable

@Serializable
data class PaginatedCollegesResponse(
    val content: List<Content?>? = null,
    val empty: Boolean? = null,
    val first: Boolean? = null,
    val last: Boolean? = null,
    val number: Int? = null,
    val numberOfElements: Int? = null,
    val pageable: Pageable? = null,
    val size: Int? = null,
    val sort: SortX? = null,
    val totalElements: Int? = null,
    val totalPages: Int? = null
)