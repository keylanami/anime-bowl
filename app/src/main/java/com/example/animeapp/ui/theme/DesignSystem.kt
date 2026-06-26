package com.example.animeapp.ui.theme

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object BowlSpacing {
    val xxs = 4.dp
    val xs = 8.dp
    val sm = 12.dp
    val md = 16.dp
    val lg = 24.dp
    val xl = 32.dp
    val xxl = 44.dp
}

object BowlRadius {
    val sm = 10.dp
    val md = 16.dp
    val lg = 22.dp
    val xl = 28.dp
    val pill = 100.dp
}

object BowlMotion {
    const val fast = 160
    const val normal = 260
    val standard = tween<Float>(durationMillis = normal, easing = FastOutSlowInEasing)
}

@Composable
fun bowlTextFieldColors(): TextFieldColors = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.55f),
    unfocusedBorderColor = Color.Transparent,
    focusedContainerColor = MaterialTheme.colorScheme.surface,
    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
    cursorColor = MaterialTheme.colorScheme.primary,
    focusedLabelColor = MaterialTheme.colorScheme.primary,
    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun bowlChipColors(): SelectableChipColors = FilterChipDefaults.filterChipColors(
    selectedContainerColor = MaterialTheme.colorScheme.primary,
    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.62f),
    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
)

@Composable
fun BowlSoftCard(
    modifier: Modifier = Modifier,
    radius: Dp = BowlRadius.lg,
    content: @Composable ColumnScope.() -> Unit
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(radius),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp),
        content = content
    )
}
