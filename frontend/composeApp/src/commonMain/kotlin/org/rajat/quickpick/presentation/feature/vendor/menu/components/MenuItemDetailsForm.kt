package org.rajat.quickpick.presentation.feature.vendor.menu.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.rajat.quickpick.presentation.components.CustomDropdown

@Composable
fun MenuItemDetailsForm(
    name: String,
    onNameChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    price: String,
    onPriceChange: (String) -> Unit,
    category: String,
    onCategoryChange: (String) -> Unit,
    quantity: String,
    onQuantityChange: (String) -> Unit,
    isVeg: Boolean,
    onIsVegChange: (Boolean) -> Unit,
    isAvailable: Boolean,
    onIsAvailableChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    categoryOptions: List<String>? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Item Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            FormInfoField(
                label = "Item Name*",
                value = name,
                onValueChange = onNameChange,
                icon = Icons.Default.Fastfood
            )

            FormInfoField(
                label = "Description",
                value = description,
                onValueChange = onDescriptionChange,
                icon = Icons.Default.Description,
                singleLine = false,
                maxLines = 3
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = price,
                    onValueChange = onPriceChange,
                    label = { Text("Price*") },
                    modifier = Modifier.weight(1f),
                    leadingIcon = {
                        Text(
                            "₹",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                FormInfoField(
                    label = "Quantity",
                    value = quantity,
                    onValueChange = onQuantityChange,
                    icon = Icons.Default.Inventory2,
                    keyboardType = KeyboardType.Number,
                    placeholder = "e.g. 10",
                    modifier = Modifier.weight(1f)
                )
            }

            CustomDropdown(
                value = category,
                onValueChange = onCategoryChange,
                label = "Select Category",
                leadingIcon = Icons.Default.Category,
                options = categoryOptions ?: emptyList(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            ToggleRow(
                text = "Item is Vegetarian",
                checked = isVeg,
                onCheckedChange = onIsVegChange
            )
            ToggleRow(
                text = "Item is Available",
                checked = isAvailable,
                onCheckedChange = onIsAvailableChange
            )
        }
    }
}