package org.rajat.quickpick.presentation.feature.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.rajat.quickpick.domain.modal.search.Vendor


@Composable
fun VendorsList(
    vendors: List<Vendor>,
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
            key = { it.id }
        ) { vendor ->
            VendorCard(
                vendor = vendor,
                onVendorClick = { onVendorClick(vendor.id ?: "") }
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}