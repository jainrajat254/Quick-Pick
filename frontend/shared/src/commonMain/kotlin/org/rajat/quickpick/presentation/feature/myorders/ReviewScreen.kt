package org.rajat.quickpick.presentation.feature.myorders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.rajat.quickpick.presentation.feature.myorders.components.OrderReviewInfo

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
        OrderReviewInfo(
            orderId = orderId,
            itemName = itemName,
            itemImageUrl = itemImageUrl,
            rating = rating,
            comment = comment,
            navController = navController,
            onRatingChange = { rating = it },
            onCommentChange = { comment = it}
        )
    }
}
