package org.rajat.quickpick.presentation.feature.vendordetail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.rajat.quickpick.data.dummy.DummyData
import org.rajat.quickpick.domain.modal.search.GetVendorByIDResponse

@Composable
fun VendorHeaderSection(
    vendor: GetVendorByIDResponse,
    rating: DummyData.VendorRating?,
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
            rating = rating
        )
    }
}
