package org.rajat.quickpick.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.rajat.quickpick.domain.modal.menuitems.GetVendorMenuByCategoryResponse
import org.rajat.quickpick.domain.modal.search.SearchMenuItemsResponse
import org.rajat.quickpick.domain.repository.MenuItemRepository
import org.rajat.quickpick.domain.repository.SearchRepository
import org.rajat.quickpick.utils.UiState

class MenuItemViewModel(
    private val menuItemRepository: MenuItemRepository,
    private val searchRepository: SearchRepository
): ViewModel() {
    private val _menuItemsState =
        MutableStateFlow<UiState<GetVendorMenuByCategoryResponse>>(UiState.Empty)
    val menuItemsState: StateFlow<UiState<GetVendorMenuByCategoryResponse>> = _menuItemsState.asStateFlow()

    private val _searchedMenuItemsState =
        MutableStateFlow<UiState<SearchMenuItemsResponse>>(UiState.Empty)
    val searchedMenuItemsState: StateFlow<UiState<SearchMenuItemsResponse>> = _searchedMenuItemsState.asStateFlow()

    private val _selectedVendorId = MutableStateFlow<String?>(null)
    val selectedVendorId: StateFlow<String?> = _selectedVendorId.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _minPrice = MutableStateFlow<Double?>(null)
    val minPrice: StateFlow<Double?> = _minPrice.asStateFlow()

    private val _maxPrice = MutableStateFlow<Double?>(null)
    val maxPrice: StateFlow<Double?> = _maxPrice.asStateFlow()

    private val _availableOnly = MutableStateFlow(true)
    val availableOnly: StateFlow<Boolean> = _availableOnly.asStateFlow()

    private val _sortBy = MutableStateFlow("name")
    val sortBy: StateFlow<String> = _sortBy.asStateFlow()

    private val _sortDirection = MutableStateFlow("ASC")
    val sortDirection: StateFlow<String> = _sortDirection.asStateFlow()

    private val _isVeg = MutableStateFlow<Boolean?>(null)
    val isVeg: StateFlow<Boolean?> = _isVeg.asStateFlow()

    private fun <T> executeWithUiState(
        stateFlow: MutableStateFlow<UiState<T>>,
        block: suspend () -> Result<T>
    ) {
        viewModelScope.launch {
            stateFlow.value = UiState.Loading
            val result = block()
            stateFlow.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Unknown error") }
            )
        }
    }
    fun setVendorAndCategory(vendorId: String?, category: String?) {
        _selectedVendorId.value = vendorId
        _selectedCategory.value = category
    }
    fun getMenuItemsByCategory(vendorId: String, category: String) {
        executeWithUiState(_menuItemsState) {
            menuItemRepository.getVendorMenuByCategory(vendorId, category)
        }
    }

    fun searchMenuItems(
        query: String? = _searchQuery.value.ifBlank { null },
        vendorId: String? = _selectedVendorId.value,
        category: String? = _selectedCategory.value,
        minPrice: Double? = _minPrice.value,
        maxPrice: Double? = _maxPrice.value,
        availableOnly: Boolean = _availableOnly.value,
        sortBy: String = _sortBy.value,
        sortDirection: String = _sortDirection.value,
        page: Int = 0,
        size: Int = 50
    ){
        executeWithUiState(_searchedMenuItemsState) {
            searchRepository.searchMenuItems(
                query = query,
                vendorId = vendorId,
                category = category,
                minPrice = minPrice,
                maxPrice = maxPrice,
                availableOnly = availableOnly,
                sortBy = sortBy,
                sortDirection = sortDirection,
                page = page,
                size = size
            )
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updatePriceRange(min: Double?, max: Double?) {
        _minPrice.value = min
        _maxPrice.value = max
    }
    fun updateAvailableOnly(available: Boolean) {
        _availableOnly.value = available
    }

    fun updateSort(sortBy: String, sortDirection: String) {
        _sortBy.value = sortBy
        _sortDirection.value = sortDirection
    }
    fun updateVegFilter(veg: Boolean?) {
        _isVeg.value = veg
    }

    fun resetFilters() {
        _searchQuery.value = ""
        _minPrice.value = null
        _maxPrice.value = null
        _availableOnly.value = true
        _sortBy.value = "name"
        _sortDirection.value = "ASC"
        _isVeg.value = null
    }

    fun resetMenuItemsState() {
        _menuItemsState.value = UiState.Empty
    }

    fun resetSearchedMenuItemsState() {
        _searchedMenuItemsState.value = UiState.Empty
    }

    fun clearVendorAndCategory() {
        _selectedVendorId.value = null
        _selectedCategory.value = null
    }
}