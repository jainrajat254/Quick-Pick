package org.rajat.quickpick.presentation.feature.vendor.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.rajat.quickpick.presentation.theme.AppColors
import quickpick.shared.generated.resources.Res
import quickpick.shared.generated.resources.cheficon
@Composable
fun VendorDashboardHeader(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box {

            Column(modifier = Modifier.fillMaxSize()) {

                Box(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxWidth()
                        .background(
                            color = AppColors.PrimaryContainer,
                            shape = RoundedCornerShape(
                                topStart = 20.dp,
                                topEnd = 20.dp
                            )
                        )
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(
                            color = AppColors.PrimaryDark,
                            shape = RoundedCornerShape(
                                bottomStart = 20.dp,
                                bottomEnd = 20.dp
                            )
                        )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 110.dp, top = 20.dp, end = 20.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "Welcome back, Chef 👋",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 9f),
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.End
                )

                Text(
                    text = "Here’s how your kitchen is performing",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 7f),
                    textAlign = TextAlign.End
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "View Profile",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.Light.OnPrimary,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.size(6.dp))

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Navigate",
                    tint = AppColors.Light.OnPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }

            Icon(
                painter = painterResource(resource = Res.drawable.cheficon),
                contentDescription = "Chef Illustration",
                tint = Color.Unspecified,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 0.dp)
                    .align(Alignment.CenterStart)
            )
        }
    }
}
