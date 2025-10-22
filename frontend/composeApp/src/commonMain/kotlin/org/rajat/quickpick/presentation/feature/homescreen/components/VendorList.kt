package org.rajat.quickpick.presentation.feature.homescreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.rajat.quickpick.data.dummy.DummyData
import org.rajat.quickpick.domain.modal.search.GetVendorByIDResponse


@Composable
fun VendorsList(
    vendors: List<GetVendorByIDResponse>,
    onVendorClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = vendors,
            key = { it.id ?: "unknown" }
        ) { vendor ->
            val rating = DummyData.getRatingByVendorId(vendor.id ?: "")

            VendorCard(
                vendor = vendor,
                rating = rating,
                onVendorClick = { onVendorClick(vendor.id ?: "") }
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}