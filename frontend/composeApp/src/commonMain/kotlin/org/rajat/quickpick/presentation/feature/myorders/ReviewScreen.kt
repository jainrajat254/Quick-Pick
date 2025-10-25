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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.rajat.quickpick.presentation.components.BasePage
import org.rajat.quickpick.presentation.feature.myorders.components.StarRatingSelector
import org.rajat.quickpick.presentation.navigation.Routes
import org.rajat.quickpick.presentation.theme.AppTheme

@Composable
fun OrderReviewScreen(
    orderId: String,
    itemName: String,
    itemImageUrl: String?,
    navController: NavHostController,
    paddingValues: PaddingValues
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
        CoilImage(
            imageModel = { itemImageUrl },
            imageOptions = ImageOptions(contentScale = ContentScale.Crop),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(180.dp)
                .clip(RoundedCornerShape(16.dp)),
            loading = {
                Box(
                    modifier = Modifier.matchParentSize()
                )
            }
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
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                    alpha = 0.3f
                ),
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            ),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = { navController.navigate(Routes.Orders.route) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancel")
            }
            Button(
                onClick = {
                    //Viewmodel method to submit review using orderId, rating, comment
                    navController.navigate(Routes.ReviewOrderConfirmation.route)
                          },
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
                orderId = "QKPK12345",
                itemName = "Paneer Curry",
                itemImageUrl = null,
                navController = rememberNavController(),
                PaddingValues(0.dp)
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
                orderId = "QKPK12345",
                itemName = "Paneer Curry",
                itemImageUrl = null,
                navController = rememberNavController(),
                PaddingValues(0.dp)
            )
        }
    }
}

