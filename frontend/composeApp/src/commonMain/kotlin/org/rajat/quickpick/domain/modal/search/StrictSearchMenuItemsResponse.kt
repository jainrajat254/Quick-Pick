package org.rajat.quickpick.domain.modal.search

import com.example.dummyproject.data.menuitems.MenuItem
import com.example.dummyproject.data.menuitems.getMyMenuItemsPaginated.Pageable
import com.example.dummyproject.data.menuitems.getMyMenuItemsPaginated.SortX
import kotlinx.serialization.Serializable

@Serializable
data class StrictSearchMenuItemsResponse(
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