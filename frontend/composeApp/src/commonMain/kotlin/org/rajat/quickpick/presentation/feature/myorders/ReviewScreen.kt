package org.rajat.quickpick.presentation.feature.myorders
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.dummyproject.R
import com.example.dummyproject.screens.myorders.components.StarRatingSelector
import com.example.dummyproject.theme.AppTheme

@Composable
fun OrderReviewScreen(
    paddingValues: PaddingValues,
    orderId: String,
    itemName: String,
    itemImageUrl: String?,
    onSubmitReview: (orderId: String, rating: Int, comment: String) -> Unit,
    onCancel: () -> Unit
) {
    var rating by remember { mutableIntStateOf(0) }
    var comment by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = itemImageUrl ?: "",
            fallback = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = itemName,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(180.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = itemName,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "We'd love to know what you think of your dish.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        StarRatingSelector(
            currentRating = rating,
            onRatingChange = { rating = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = comment,
            onValueChange = { comment = it },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp),
            label = { Text("Leave us your comment!") },
            placeholder = { Text("Write here...") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                cursorColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha=0.3f),
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha=0.3f)
            ),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancel")
            }
            Button(
                onClick = { onSubmitReview(orderId, rating, comment) },
                modifier = Modifier.weight(1f),
                enabled = rating > 0
            ) {
                Text("Submit")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}


// --- PREVIEWS ---
@Preview(showBackground = true, name = "Order Review Light")
@Composable
fun OrderReviewScreenLightPreview() {
    AppTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            OrderReviewScreen(
                paddingValues = PaddingValues(0.dp),
                orderId = "QKPK12345",
                itemName = "Paneer Curry",
                itemImageUrl = null,
                onSubmitReview = { _, _, _ -> },
                onCancel = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Order Review Dark")
@Composable
fun OrderReviewScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            OrderReviewScreen(
                paddingValues = PaddingValues(0.dp),
                orderId = "QKPK12345",
                itemName = "Paneer Curry",
                itemImageUrl = null,
                onSubmitReview = { _, _, _ -> },
                onCancel = {}
            )
        }
    }
}
