package org.rajat.quickpick.presentation.feature.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.rajat.quickpick.presentation.feature.cart.components.CreateOrderBody
import org.rajat.quickpick.presentation.feature.cart.components.OrderInfoHeader
import org.rajat.quickpick.presentation.feature.cart.components.PaymentMethod
import org.rajat.quickpick.presentation.feature.cart.components.SpecialInstructions

@Composable
fun CheckoutScreen(
    paddingValues: PaddingValues,
    cartItems: List<CartItem>,
    totalAmount: Double,
    isLoading: Boolean,
    navController: NavHostController,
) {
    var specialInstructions by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        OrderInfoHeader(
            cartItems = cartItems,
            totalAmount = totalAmount,
        )

        Spacer(modifier = Modifier.height(24.dp))

        //Payment Method
        PaymentMethod()

        //Special Instructions
        SpecialInstructions(
            specialInstructions = specialInstructions,
            onSpecialInstructionsChange = { specialInstructions = it }
        )

        Spacer(modifier = Modifier.weight(1f))
        CreateOrderBody(
            totalAmount=totalAmount,
            navController = navController,
            specialInstructions = specialInstructions,
            isLoading=false,
            cartItems=cartItems
        )

    }
}

