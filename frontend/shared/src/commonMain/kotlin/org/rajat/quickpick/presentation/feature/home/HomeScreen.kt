package org.rajat.quickpick.presentation.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import org.rajat.quickpick.domain.modal.search.GetAllVendorsInCollegeResponse
import org.rajat.quickpick.presentation.components.CustomLoader
import org.rajat.quickpick.presentation.components.ErrorState
import org.rajat.quickpick.presentation.feature.home.components.EmptyState
import org.rajat.quickpick.presentation.feature.home.components.VendorsList
import org.rajat.quickpick.presentation.navigation.AppScreenUser
import org.rajat.quickpick.presentation.viewmodel.HomeViewModel
import org.rajat.quickpick.presentation.viewmodel.MenuItemViewModel
import org.rajat.quickpick.presentation.viewmodel.ReviewViewModel
import org.rajat.quickpick.presentation.viewmodel.VendorViewModel
import org.rajat.quickpick.utils.BackHandler
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.exitApp
import org.rajat.quickpick.utils.toast.showToast
import org.rajat.quickpick.utils.ErrorUtils

@OptIn(ExperimentalTime::class)
@Composable
fun HomeScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    homeViewModel: HomeViewModel,
    vendorViewModel: VendorViewModel,
    reviewViewModel: ReviewViewModel,
    menuItemViewModel: MenuItemViewModel
) {
    var backPressedTime by remember { mutableStateOf(0L) }

    val vendorsState by homeViewModel.vendorsInCollegeState.collectAsState()

    BackHandler(enabled = true) {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        if (currentTime - backPressedTime < 2000) {
            exitApp()
        } else {
            backPressedTime = currentTime
            showToast("Press back again to exit")
        }
    }

    LaunchedEffect(Unit) {
        homeViewModel.getVendorsInCollege()
    }

    LaunchedEffect(vendorsState) {
        when (vendorsState) {
            is UiState.Error -> {
                val raw = (vendorsState as UiState.Error).message
                val message = ErrorUtils.sanitizeError(raw)
                showToast(message)
            }
            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        when (vendorsState) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CustomLoader()
                }
            }

            UiState.Empty -> {
                EmptyState(
                    modifier = Modifier.fillMaxSize()
                )
            }

            is UiState.Error -> {
                ErrorState(
                    message = ErrorUtils.sanitizeError((vendorsState as UiState.Error).message),
                    modifier = Modifier.fillMaxSize()
                )
            }

            is UiState.Success -> {
                val vendors = (vendorsState as UiState.Success<GetAllVendorsInCollegeResponse>).data

                if (vendors.vendors.isEmpty()) {
                    EmptyState(
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    VendorsList(
                        vendors = vendors.vendors,
                        onVendorClick = { vendorId ->
                            navController.navigate(AppScreenUser.VendorDetail(vendorId))
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
