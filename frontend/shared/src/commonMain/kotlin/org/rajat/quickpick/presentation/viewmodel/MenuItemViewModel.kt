package org.rajat.quickpick.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.rajat.quickpick.domain.modal.menuitems.CreateMenuItemRequest
import org.rajat.quickpick.domain.modal.menuitems.CreateMenuItemResponse
import org.rajat.quickpick.domain.modal.menuitems.DeleteMenuItemResponse
import org.rajat.quickpick.domain.modal.menuitems.GetVendorMenuByCategoryResponse
import org.rajat.quickpick.domain.modal.menuitems.ToggleAvailabilityResponse
import org.rajat.quickpick.domain.modal.menuitems.UpdateMenuItemRequest
import org.rajat.quickpick.domain.modal.menuitems.UpdateMenuItemResponse
import org.rajat.quickpick.domain.modal.menuitems.getMyMenuItemsPaginated.GetMyMenuItemsPaginatedResponse
import org.rajat.quickpick.domain.modal.search.SearchMenuItemsResponse
import org.rajat.quickpick.domain.repository.MenuItemRepository
import org.rajat.quickpick.domain.repository.SearchRepository
import org.rajat.quickpick.utils.UiState
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class MenuItemViewModel(
    private val menuItemRepository: MenuItemRepository,
    private val searchRepository: SearchRepository
) : ViewModel() {
    private val _menuItemsState =
        MutableStateFlow<UiState<GetVendorMenuByCategoryResponse>>(UiState.Empty)
    val menuItemsState: StateFlow<UiState<GetVendorMenuByCategoryResponse>> =
        _menuItemsState.asStateFlow()

    private val _myMenuItemsState =
        MutableStateFlow<UiState<GetMyMenuItemsPaginatedResponse>>(UiState.Empty)
    val myMenuItemsState: StateFlow<UiState<GetMyMenuItemsPaginatedResponse>> =
        _myMenuItemsState.asStateFlow()

    private val _searchedMenuItemsState =
        MutableStateFlow<UiState<SearchMenuItemsResponse>>(UiState.Empty)
    val searchedMenuItemsState: StateFlow<UiState<SearchMenuItemsResponse>> =
        _searchedMenuItemsState.asStateFlow()

    private val _toggleAvailabilityState =
        MutableStateFlow<UiState<ToggleAvailabilityResponse>>(UiState.Empty)
    val toggleAvailabilityState: StateFlow<UiState<ToggleAvailabilityResponse>> =
        _toggleAvailabilityState.asStateFlow()

    private val _createMenuItemState =
        MutableStateFlow<UiState<CreateMenuItemResponse>>(UiState.Empty)
    val createMenuItemState: StateFlow<UiState<CreateMenuItemResponse>> =
        _createMenuItemState.asStateFlow()

    private val _singleMenuItemState =
        MutableStateFlow<UiState<CreateMenuItemResponse>>(UiState.Empty)
    val singleMenuItemState: StateFlow<UiState<CreateMenuItemResponse>> =
        _singleMenuItemState.asStateFlow()

    private val _updateMenuItemState =
        MutableStateFlow<UiState<UpdateMenuItemResponse>>(UiState.Empty)
    val updateMenuItemState: StateFlow<UiState<UpdateMenuItemResponse>> =
        _updateMenuItemState.asStateFlow()

    private val _deleteMenuItemState =
        MutableStateFlow<UiState<DeleteMenuItemResponse>>(UiState.Empty)
    val deleteMenuItemState: StateFlow<UiState<DeleteMenuItemResponse>> =
        _deleteMenuItemState.asStateFlow()

    private val _vendorMenuState = MutableStateFlow<UiState<List<CreateMenuItemResponse>>>(UiState.Empty)
    val vendorMenuState: StateFlow<UiState<List<CreateMenuItemResponse>>> = _vendorMenuState.asStateFlow()

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

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private var cachedMyMenuItems: GetMyMenuItemsPaginatedResponse? = null
    private var myMenuItemsCacheTime: Long = 0
    private val cachedVendorMenu: MutableMap<String, List<CreateMenuItemResponse>> = mutableMapOf()
    private val vendorMenuCacheTime: MutableMap<String, Long> = mutableMapOf()
    private val cachedMenuItemsByCategory: MutableMap<String, GetVendorMenuByCategoryResponse> = mutableMapOf()
    private val menuItemsByCategoryCacheTime: MutableMap<String, Long> = mutableMapOf()
    private var currentMenuCategoryKey: String? = null
    private val cacheValidityDuration = 60_000L

    private fun isCacheValid(cacheTime: Long): Boolean {
        return (Clock.System.now().toEpochMilliseconds() - cacheTime) < cacheValidityDuration
    }

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

    fun getMenuItemsByCategory(vendorId: String, category: String, forceRefresh: Boolean = false) {
        val cacheKey = "${vendorId}_$category"
        val cachedTime = menuItemsByCategoryCacheTime[cacheKey] ?: 0

        if (!forceRefresh && cachedMenuItemsByCategory.containsKey(cacheKey) && isCacheValid(cachedTime)) {
            if (_menuItemsState.value is UiState.Success && currentMenuCategoryKey == cacheKey) {
                return
            }
            currentMenuCategoryKey = cacheKey
            _menuItemsState.value = UiState.Success(cachedMenuItemsByCategory[cacheKey]!!)
            return
        }

        viewModelScope.launch {
            _menuItemsState.value = UiState.Loading
            val result = menuItemRepository.getVendorMenuByCategory(vendorId, category)
            result.fold(
                onSuccess = { data ->
                    cachedMenuItemsByCategory[cacheKey] = data
                    menuItemsByCategoryCacheTime[cacheKey] = Clock.System.now().toEpochMilliseconds()
                    currentMenuCategoryKey = cacheKey
                    _menuItemsState.value = UiState.Success(data)
                },
                onFailure = { error ->
                    _menuItemsState.value = UiState.Error(error.message ?: "Unknown error")
                }
            )
        }
    }

    fun isMenuCategoryDataLoadedFor(vendorId: String, category: String): Boolean {
        val cacheKey = "${vendorId}_$category"
        return _menuItemsState.value is UiState.Success && currentMenuCategoryKey == cacheKey
    }

    private var currentVendorMenuId: String? = null
    private val vendorMenuLoadingStates: MutableMap<String, Boolean> = mutableMapOf()

    fun getVendorMenu(vendorId: String, forceRefresh: Boolean = false) {
        val isLoading = vendorMenuLoadingStates[vendorId] ?: false
        val cachedTime = vendorMenuCacheTime[vendorId] ?: 0

        if (isLoading && !forceRefresh) {
            return
        }

        if (!forceRefresh && cachedVendorMenu.containsKey(vendorId) && isCacheValid(cachedTime)) {
            if (_vendorMenuState.value is UiState.Success && currentVendorMenuId == vendorId) {
                return
            }
            currentVendorMenuId = vendorId
            _vendorMenuState.value = UiState.Success(cachedVendorMenu[vendorId]!!)
            return
        }

        vendorMenuLoadingStates[vendorId] = true
        viewModelScope.launch {
            _vendorMenuState.value = UiState.Loading
            val result = menuItemRepository.getVendorMenu(vendorId)
            result.fold(
                onSuccess = { list ->
                    val filteredList = list.filterNotNull()
                    cachedVendorMenu[vendorId] = filteredList
                    vendorMenuCacheTime[vendorId] = Clock.System.now().toEpochMilliseconds()
                    currentVendorMenuId = vendorId
                    _vendorMenuState.value = UiState.Success(filteredList)
                },
                onFailure = { throwable ->
                    _vendorMenuState.value = UiState.Error(throwable.message ?: "Unknown error")
                }
            )
            vendorMenuLoadingStates[vendorId] = false
        }
    }

    fun isVendorMenuDataLoadedFor(vendorId: String): Boolean {
        return _vendorMenuState.value is UiState.Success && currentVendorMenuId == vendorId
    }

    fun getVendorMenuByCategories(vendorId: String, categories: List<String>) {
        viewModelScope.launch {
            _vendorMenuState.value = UiState.Loading
            val aggregated = mutableListOf<CreateMenuItemResponse>()
            var firstErrorMessage: String? = null
            for (category in categories) {
                val res = menuItemRepository.getVendorMenuByCategory(vendorId, category)
                res.fold(onSuccess = { resp ->
                    val items = resp.menuItems ?: emptyList()
                    aggregated.addAll(items.filterNotNull())
                }, onFailure = { thr ->
                    if (firstErrorMessage == null) firstErrorMessage = thr.message
                })
            }
            if (aggregated.isNotEmpty()) {
                _vendorMenuState.value = UiState.Success(aggregated)
            } else if (firstErrorMessage != null) {
                _vendorMenuState.value = UiState.Error(firstErrorMessage)
            } else {
                _vendorMenuState.value = UiState.Success(aggregated)
            }
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
    ) {
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

    fun resetMyMenuItemsState() {
        _myMenuItemsState.value = UiState.Empty
    }

    fun resetSearchedMenuItemsState() {
        _searchedMenuItemsState.value = UiState.Empty
    }

    fun resetToggleAvailabilityState() {
        _toggleAvailabilityState.value = UiState.Empty
    }

    fun resetCreateMenuItemState() {
        _createMenuItemState.value = UiState.Empty
    }

    fun resetSingleMenuItemState() {
        _singleMenuItemState.value = UiState.Empty
    }

    fun resetUpdateMenuItemState() {
        _updateMenuItemState.value = UiState.Empty
    }

    fun resetDeleteMenuItemState() {
        _deleteMenuItemState.value = UiState.Empty
    }

    fun clearVendorAndCategory() {
        _selectedVendorId.value = null
        _selectedCategory.value = null
    }

    fun getMyMenuItems(page: Int = 0, size: Int = 50, forceRefresh: Boolean = false) {
        if (!forceRefresh && cachedMyMenuItems != null && isCacheValid(myMenuItemsCacheTime)) {
            if (_myMenuItemsState.value is UiState.Success) {
                return
            }
            _myMenuItemsState.value = UiState.Success(cachedMyMenuItems!!)
            return
        }

        viewModelScope.launch {
            if (forceRefresh) {
                _isRefreshing.value = true
            } else {
                _myMenuItemsState.value = UiState.Loading
            }

            val result = menuItemRepository.getMyMenuItems(page, size)
            result.fold(
                onSuccess = { data ->
                    cachedMyMenuItems = data
                    myMenuItemsCacheTime = Clock.System.now().toEpochMilliseconds()
                    _myMenuItemsState.value = UiState.Success(data)
                },
                onFailure = { error ->
                    _myMenuItemsState.value = UiState.Error(error.message ?: "Unknown error")
                }
            )
            _isRefreshing.value = false
        }
    }

    fun toggleMenuItemAvailability(menuItemId: String) {
        viewModelScope.launch {
            _toggleAvailabilityState.value = UiState.Loading
            val result = menuItemRepository.toggleMenuItemAvailability(menuItemId)
            result.fold(
                onSuccess = { data ->
                    invalidateMyMenuItemsCache()
                    _toggleAvailabilityState.value = UiState.Success(data)
                },
                onFailure = { error ->
                    _toggleAvailabilityState.value = UiState.Error(error.message ?: "Unknown error")
                }
            )
        }
    }

    fun createMenuItem(request: CreateMenuItemRequest) {
        viewModelScope.launch {
            _createMenuItemState.value = UiState.Loading
            val result = menuItemRepository.createMenuItem(request)
            result.fold(
                onSuccess = { data ->
                    invalidateMyMenuItemsCache()
                    _createMenuItemState.value = UiState.Success(data)
                },
                onFailure = { error ->
                    _createMenuItemState.value = UiState.Error(error.message ?: "Unknown error")
                }
            )
        }
    }

    fun getMenuItemById(menuItemId: String) {
        executeWithUiState(_singleMenuItemState) {
            menuItemRepository.getMenuItemById(menuItemId)
        }
    }

    fun updateMenuItem(menuItemId: String, request: UpdateMenuItemRequest) {
        viewModelScope.launch {
            _updateMenuItemState.value = UiState.Loading
            val result = menuItemRepository.updateMenuItem(menuItemId, request)
            result.fold(
                onSuccess = { data ->
                    invalidateMyMenuItemsCache()
                    _updateMenuItemState.value = UiState.Success(data)
                },
                onFailure = { error ->
                    _updateMenuItemState.value = UiState.Error(error.message ?: "Unknown error")
                }
            )
        }
    }

    fun deleteMenuItem(menuItemId: String) {
        viewModelScope.launch {
            _deleteMenuItemState.value = UiState.Loading
            val result = menuItemRepository.deleteMenuItem(menuItemId)
            result.fold(
                onSuccess = { data ->
                    invalidateMyMenuItemsCache()
                    _deleteMenuItemState.value = UiState.Success(data)
                },
                onFailure = { error ->
                    _deleteMenuItemState.value = UiState.Error(error.message ?: "Unknown error")
                }
            )
        }
    }

    fun invalidateMyMenuItemsCache() {
        cachedMyMenuItems = null
        myMenuItemsCacheTime = 0
    }

    fun invalidateVendorMenuCache(vendorId: String) {
        cachedVendorMenu.remove(vendorId)
        vendorMenuCacheTime.remove(vendorId)
    }

    fun invalidateAllCache() {
        cachedMyMenuItems = null
        myMenuItemsCacheTime = 0
        cachedVendorMenu.clear()
        vendorMenuCacheTime.clear()
        cachedMenuItemsByCategory.clear()
        menuItemsByCategoryCacheTime.clear()
        currentMenuCategoryKey = null
    }
}