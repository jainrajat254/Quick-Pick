package org.rajat.quickpick.presentation.feature.vendor.reviews

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.compose.koinInject
import org.rajat.quickpick.domain.modal.review.getPaginatedReviewsforVendor.Content
import org.rajat.quickpick.presentation.viewmodel.ReviewViewModel
import org.rajat.quickpick.utils.UiState
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendorReviewsScreen(
    navController: NavController,
    vendorId: String,
    reviewViewModel: ReviewViewModel = koinInject(),
    onBackClick: () -> Unit = { navController.navigateUp() }
) {
    val ratingState by reviewViewModel.vendorRatingState.collectAsState()
    val reviewsState by reviewViewModel.vendorReviewsState.collectAsState()

    LaunchedEffect(vendorId) {
        reviewViewModel.getVendorRating(vendorId)
        reviewViewModel.getVendorReviewsPaginated(vendorId, page = 0, size = 50)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reviews", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            when (ratingState) {
                is UiState.Success -> {
                    val rating = (ratingState as UiState.Success).data
                    if (rating.averageRating != null && rating.totalReviews != null) {
                        Text(
                            text = "${(rating.averageRating * 10).roundToInt() / 10.0} average from ${rating.totalReviews} reviews",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                is UiState.Loading -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                is UiState.Error -> Text("Failed to load rating", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
                UiState.Empty -> {}
            }

            Spacer(modifier = Modifier.height(8.dp))

            when (reviewsState) {
                is UiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                is UiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text((reviewsState as UiState.Error).message ?: "Failed to load reviews", color = MaterialTheme.colorScheme.error)
                }
                is UiState.Success -> {
                    val reviews = (reviewsState as UiState.Success).data.content?.filterNotNull() ?: emptyList()
                    if (reviews.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("No reviews yet") }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(reviews, key = { it.id ?: it.userId ?: it.vendorId ?: it.hashCode().toString() }) { review ->
                                ReviewItemCard(review)
                            }
                        }
                    }
                }
                UiState.Empty -> {}
            }
        }
    }
}

@Composable
private fun ReviewItemCard(review: Content) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(review.userName ?: "Anonymous", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                review.rating?.let {
                    Surface(shape = MaterialTheme.shapes.small, color = MaterialTheme.colorScheme.primaryContainer) {
                        Text(
                            text = "$it★",
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
            if (!review.comment.isNullOrBlank()) {
                Text(review.comment!!, style = MaterialTheme.typography.bodySmall)
            }
            review.createdAt?.let { Text(it, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        }
    }
}
