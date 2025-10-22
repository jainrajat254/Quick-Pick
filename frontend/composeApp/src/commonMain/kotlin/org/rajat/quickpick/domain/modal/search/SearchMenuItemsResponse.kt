package org.rajat.quickpick.domain.modal.search


import kotlinx.serialization.Serializable
import org.rajat.quickpick.domain.modal.menuitems.MenuItem
import org.rajat.quickpick.domain.modal.adminManagement.getAllUsers.Pageable
import org.rajat.quickpick.domain.modal.adminManagement.getAllUsers.SortX

@Serializable
data class SearchMenuItemsResponse(
    val content: List<MenuItem?>?=null,
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