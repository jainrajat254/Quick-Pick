package org.rajat.quickpick.presentation.feature.menuitem.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSortBottomSheet(
    minPrice: Double?,
    maxPrice: Double?,
    availableOnly: Boolean,
    sortBy: String,
    sortDirection: String,
    onMinPriceChange: (Double?) -> Unit,
    onMaxPriceChange: (Double?) -> Unit,
    onAvailableOnlyChange: (Boolean) -> Unit,
    onSortChange: (String, String) -> Unit,
    onApply: () -> Unit,
    onReset: () -> Unit,
    onDismiss: () -> Unit
) {
    var minPriceText by remember(minPrice) { mutableStateOf(minPrice?.toString() ?: "") }
    var maxPriceText by remember(maxPrice) { mutableStateOf(maxPrice?.toString() ?: "") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filter & Sort",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Price Range",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = minPriceText,
                    onValueChange = {
                        minPriceText = it
                        onMinPriceChange(it.toDoubleOrNull())
                    },
                    label = { Text("Min Price") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = maxPriceText,
                    onValueChange = {
                        maxPriceText = it
                        onMaxPriceChange(it.toDoubleOrNull())
                    },
                    label = { Text("Max Price") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Available Only",
                    style = MaterialTheme.typography.bodyLarge
                )
                Switch(
                    checked = availableOnly,
                    onCheckedChange = onAvailableOnlyChange
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Sort By",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            val sortOptions = listOf(
                "Name" to "name",
                "Price" to "price"
            )

            sortOptions.forEach { (label, value) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = sortBy == value,
                            onClick = { onSortChange(value, sortDirection) }
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = sortBy == value,
                        onClick = { onSortChange(value, sortDirection) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = label)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Sort Direction",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            val directionOptions = listOf(
                "Ascending" to "ASC",
                "Descending" to "DESC"
            )

            directionOptions.forEach { (label, value) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = sortDirection == value,
                            onClick = { onSortChange(sortBy, value) }
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = sortDirection == value,
                        onClick = { onSortChange(sortBy, value) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = label)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        minPriceText = ""
                        maxPriceText = ""
                        onReset()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Reset")
                }
                Button(
                    onClick = {
                        onApply()
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Apply")
                }
            }
        }
    }
}

