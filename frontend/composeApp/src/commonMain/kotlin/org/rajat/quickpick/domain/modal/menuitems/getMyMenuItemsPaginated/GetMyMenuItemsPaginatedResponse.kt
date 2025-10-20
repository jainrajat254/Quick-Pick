package org.rajat.quickpick.domain.modal.menuitems.getMyMenuItemsPaginated

import kotlinx.serialization.Serializable
import org.rajat.quickpick.domain.modal.college.paginatedColleges.Content

@Serializable
data class GetMyMenuItemsPaginatedResponse(
    val content: List<Content?>?=null,
    val empty: Boolean?=null,
    val first: Boolean?=null,
    val last: Boolean?=null,
    val number: Int?=null,
    val numberOfElements: Int?=null,
    val pageable: Pageable?=null,
    val size: Int?=null,
    val sort: SortX?=null,
    val totalElements: Int?=null,
    val totalPages: Int?=null
)