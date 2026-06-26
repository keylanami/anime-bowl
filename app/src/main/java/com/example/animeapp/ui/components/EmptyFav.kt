package com.example.animeapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.animeapp.ui.theme.BowlSpacing

@Composable
fun EmptyFavView(
    title: String = "Your bowl is empty",
    message: String = "Search for an anime and save your first review.",
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.FolderOpen,
            contentDescription = "Empty",
            modifier = Modifier.size(58.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(BowlSpacing.md))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(BowlSpacing.xs))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = BowlSpacing.xl)
        )
        if (actionLabel != null && onAction != null) {
            Spacer(modifier = Modifier.height(BowlSpacing.lg))
            Button(onClick = onAction) {
                Text(actionLabel)
            }
        }
    }
}
