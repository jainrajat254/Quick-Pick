package org.rajat.quickpick.presentation.feature.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import org.rajat.quickpick.domain.modal.search.Vendor
import org.rajat.quickpick.presentation.viewmodel.ReviewViewModel
import org.rajat.quickpick.utils.UiState

@Composable
fun VendorCard(
    vendor: Vendor,
    modifier: Modifier = Modifier,
    onVendorClick: () -> Unit,
    reviewViewModel: ReviewViewModel = koinInject()
) {
    val ratingState by reviewViewModel.getVendorRatingState(vendor.id ?: "").collectAsState()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onVendorClick),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            StoreImage(
                imageUrl = vendor.profileImageUrl,
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Column {
                    vendor.storeName?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    if (!vendor.vendorDescription.isNullOrBlank()) {
                        Text(
                            text = vendor.vendorDescription,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        when (ratingState) {
                            is UiState.Success -> {
                                val rating = (ratingState as UiState.Success).data
                                if (rating.averageRating != null && rating.totalReviews != null) {
                                    RatingBadge(
                                        rating = (kotlin.math.round(rating.averageRating * 10) / 10.0),
                                        reviewCount = rating.totalReviews
                                    )
                                }
                            }
                            is UiState.Loading -> {
                                Text("Loading...", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            is UiState.Error -> {
                                Text("N/A", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error)
                            }
                            UiState.Empty -> {}
                        }
                        if (vendor.address?.length!! < 15) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f, fill = false)) {
                                Icon(imageVector = Icons.Outlined.LocationOn, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = vendor.address?.take(20) ?: "",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}