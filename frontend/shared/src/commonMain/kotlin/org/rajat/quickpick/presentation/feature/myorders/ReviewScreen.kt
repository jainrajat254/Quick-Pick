package org.rajat.quickpick.presentation.feature.myorders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.coroutines.flow.first
import org.koin.compose.koinInject
import org.rajat.quickpick.presentation.feature.myorders.components.OrderReviewInfo
import org.rajat.quickpick.presentation.navigation.AppScreenUser
import org.rajat.quickpick.presentation.viewmodel.HomeViewModel
import org.rajat.quickpick.presentation.viewmodel.OrderViewModel
import org.rajat.quickpick.presentation.viewmodel.ReviewViewModel
import org.rajat.quickpick.presentation.viewmodel.VendorViewModel
import org.rajat.quickpick.domain.modal.review.CreateReviewStudentRequest
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.toast.showToast
import org.rajat.quickpick.utils.ErrorUtils
import co.touchlab.kermit.Logger

private val logger = Logger.withTag("ReviewScreen")

@Composable
fun OrderReviewScreen(
    orderId: String,
    itemName: String,
    itemImageUrl: String?,
    navController: NavHostController,
    paddingValues: PaddingValues,
    orderViewModel: OrderViewModel = koinInject(),
    reviewViewModel: ReviewViewModel = koinInject(),
    homeViewModel: HomeViewModel = koinInject(),
    vendorViewModel: VendorViewModel = koinInject()
) {
    var rating by remember { mutableIntStateOf(0) }
    var comment by remember { mutableStateOf("") }

    val orderByIdState by orderViewModel.orderByIdState.collectAsState()

    LaunchedEffect(orderId) {
        orderViewModel.getOrderById(orderId)
    }

    val createState by reviewViewModel.createReviewState.collectAsState()
    LaunchedEffect(createState) {
        if (createState is UiState.Success) {
            val vendorId = (orderByIdState as? UiState.Success)?.data?.vendorId
            if (!vendorId.isNullOrBlank()) {
                reviewViewModel.refreshVendorRating(vendorId)
                reviewViewModel.getVendorReviewsPaginated(vendorId, page = 0, size = 25)

                homeViewModel.getVendorsInCollege()
                vendorViewModel.getVendorsDetails(vendorId)

                withTimeoutOrNull(3000L) {
                    val ratingFlow = reviewViewModel.getVendorRatingState(vendorId)
                    ratingFlow.first { it is UiState.Success }
                }
            }

            reviewViewModel.resetCreateReviewState()

            navController.navigate(AppScreenUser.ReviewOrderConfirmation) {
                popUpTo(navController.graph.startDestinationId) { inclusive = false }
            }
        } else if (createState is UiState.Error) {
            val raw = (createState as UiState.Error).message
            logger.e { "Review create error: $raw" }
            showToast(ErrorUtils.sanitizeError(raw))
        }
    }

    val vendorId = (orderByIdState as? UiState.Success)?.data?.vendorId

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OrderReviewInfo(
            orderId = orderId,
            itemName = itemName,
            itemImageUrl = itemImageUrl,
            rating = rating,
            comment = comment,
            navController = navController,
            onRatingChange = { rating = it },
            onCommentChange = { comment = it },
            onSubmit = {
                if (vendorId.isNullOrBlank()) {
                    showToast("Unable to submit review: vendor information missing")
                    return@OrderReviewInfo
                }
                val payload = CreateReviewStudentRequest(
                    comment = comment,
                    orderId = orderId,
                    rating = rating,
                    vendorId = vendorId
                )
                reviewViewModel.createReview(payload)
            }
        )
    }
}
