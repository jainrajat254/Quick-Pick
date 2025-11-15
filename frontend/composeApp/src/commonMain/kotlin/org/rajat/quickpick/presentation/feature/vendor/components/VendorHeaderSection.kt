package org.rajat.quickpick.presentation.feature.vendor.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.rajat.quickpick.domain.modal.search.GetVendorByIDResponse
import org.rajat.quickpick.domain.modal.review.GetVendorRatingStatsResponse
import org.rajat.quickpick.utils.UiState

@Composable
fun VendorHeaderSection(
    vendor: GetVendorByIDResponse,
    ratingState: UiState<GetVendorRatingStatsResponse>,
    onRatingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        StoreBannerImage(
            imageUrl = vendor.profileImageUrl,
            storeName = vendor.storeName ?: "Store"
        )

        Spacer(modifier = Modifier.height(16.dp))

        StoreInfoCard(
            vendor = vendor,
            ratingState = ratingState,
            onRatingClick = onRatingClick
        )
    }
}
