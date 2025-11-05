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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.rajat.quickpick.presentation.feature.myorders.components.ReasonRow
import org.rajat.quickpick.presentation.navigation.Routes

@Composable
fun CancelOrderScreen(
    orderId: String,
    isLoading: Boolean,
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    val reasons = listOf(
        "Order placed by mistake",
        "Item(s) unavailable",
        "Changed my mind",
        "Delivery time too long"
    )
    var selectedReason by remember { mutableStateOf<String?>(null) }
    var otherReasonText by remember { mutableStateOf("") }
    val isOtherSelected = selectedReason == "Others"
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                "Please select a reason for cancellation:",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    reasons.forEach { reason ->
                        ReasonRow(
                            text = reason,
                            isSelected = selectedReason == reason,
                            onSelected = { selectedReason = reason }
                        )
                    }
                    ReasonRow(
                        text = "Others",
                        isSelected = isOtherSelected,
                        onSelected = { selectedReason = "Others" }
                    )
                }
            }

            if (isOtherSelected) {
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedTextField(
                    value = otherReasonText,
                    onValueChange = { otherReasonText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp),
                    label = { Text("Please specify your reason") },
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
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                            alpha = 0.3f
                        )
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val finalReason =
                        if (isOtherSelected) otherReasonText.takeIf { it.isNotBlank() } else selectedReason
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = "Are you sure you want to cancel this order?",
                            actionLabel = "Confirm",
                            duration = SnackbarDuration.Short
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            //Viewmodel method to update the order state to cancel with orderId, finalReason
                            navController.navigate(Routes.CancelOrderConfirmation.route)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !isLoading && (selectedReason != null && (!isOtherSelected || otherReasonText.isNotBlank())),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Submit", fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            snackbar = { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    actionColor = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        )
    }
}

