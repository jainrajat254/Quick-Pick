package org.rajat.quickpick.presentation.feature.myorders.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import org.rajat.quickpick.presentation.navigation.AppScreenUser

@Composable
fun OrderReviewInfo(
    orderId: String,
    itemName: String,
    itemImageUrl: String?,
    rating : Int,
    comment : String,
    navController: NavHostController,
    onRatingChange : (Int) -> Unit,
    onCommentChange : (String) -> Unit,
    onSubmit: () -> Unit

){
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
        onRatingChange = onRatingChange
    )

    Spacer(modifier = Modifier.height(24.dp))

    OutlinedTextField(
        value = comment,
        onValueChange = onCommentChange,
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
            onClick = { navController.navigate(AppScreenUser.Orders) },
            modifier = Modifier.weight(1f)
        ) {
            Text("Cancel")
        }
        Button(
            onClick = {
                onSubmit()
            },
            modifier = Modifier.weight(1f),
            enabled = rating > 0
        ) {
            Text("Submit")
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}
