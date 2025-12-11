package org.rajat.quickpick.presentation.feature.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.rajat.quickpick.data.dummy.dummyFaqList
import org.rajat.quickpick.presentation.feature.profile.components.FaqItemCard
import org.rajat.quickpick.presentation.navigation.AppScreenUser

@Composable
fun HelpAndFaqsScreen(
    paddingValues: PaddingValues,
    navController: NavController
) {
    var expandedItemId by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f).padding(top=16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(dummyFaqList, key = { it.id }) { faqItem ->
                FaqItemCard(
                    question = faqItem.question,
                    answer = faqItem.answer,
                    isExpanded = expandedItemId == faqItem.id,
                    onClick = {
                        expandedItemId = if (expandedItemId == faqItem.id) null else faqItem.id
                    }
                )
            }
        }

        //Fallback Contact Us section
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "Can't find your answer?",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Get in touch with our support team for help.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick ={
                navController.navigate(AppScreenUser.ContactUs)
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(50.dp)
                .align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(50)
        ) {
            Text("Contact Us", fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
