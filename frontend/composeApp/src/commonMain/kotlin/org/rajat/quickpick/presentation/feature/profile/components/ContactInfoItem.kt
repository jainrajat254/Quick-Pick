package org.rajat.quickpick.presentation.feature.profile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp

@Composable
fun ContactInfoItem(
    icon: ImageVector,
    label: String,
    value: String,
    onClick: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val uriHandler = LocalUriHandler.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                when (label) {
                    "Email Us" -> {
                        try { uriHandler.openUri("mailto:$value") } catch (e: Exception) { /* Handle error */ }
                    }
                    "Call Us" -> {
                        try { uriHandler.openUri("tel:$value") } catch (e: Exception) { /* Handle error */ }
                    }
                    "Our Address" -> {
                        clipboardManager.setText(AnnotatedString(value))
                    }
                    else -> onClick()
                }
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        ContactInfoItemData(icon,label,value)
    }
}


