package org.rajat.quickpick.presentation.feature.vendor.menu.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun ProfileInfoField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit = {},
    icon: ImageVector,
    isEditable: Boolean,
    placeholder: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    maxLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { if (placeholder != null) Text(placeholder) },
        leadingIcon = { Icon(icon, contentDescription = label) },
        enabled = isEditable,
        readOnly = !isEditable,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = singleLine,
        maxLines = maxLines,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    )
}