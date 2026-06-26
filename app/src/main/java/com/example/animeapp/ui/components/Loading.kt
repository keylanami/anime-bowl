package com.example.animeapp.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.animeapp.ui.theme.BowlRadius
import com.example.animeapp.ui.theme.BowlSpacing

@Composable
fun LoadingView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(BowlSpacing.md),
        verticalArrangement = Arrangement.spacedBy(BowlSpacing.md)
    ) {
        repeat(4) {
            LoadingCardPlaceholder()
        }
    }
}

@Composable
fun LoadingCardPlaceholder(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val alpha = transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.78f,
        animationSpec = infiniteRepeatable(
            animation = tween(820),
            repeatMode = RepeatMode.Reverse
        ),
        label = "placeholderAlpha"
    )
    val color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha.value)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(BowlRadius.lg))
            .background(MaterialTheme.colorScheme.surface)
            .padding(BowlSpacing.sm)
    ) {
        Spacer(
            Modifier
                .size(width = 76.dp, height = 104.dp)
                .clip(RoundedCornerShape(BowlRadius.md))
                .background(color)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = BowlSpacing.md),
            verticalArrangement = Arrangement.spacedBy(BowlSpacing.xs)
        ) {
            Spacer(Modifier.fillMaxWidth(0.78f).height(16.dp).clip(RoundedCornerShape(50)).background(color))
            Spacer(Modifier.fillMaxWidth(0.42f).height(12.dp).clip(RoundedCornerShape(50)).background(color))
            Spacer(Modifier.fillMaxWidth().height(38.dp).clip(RoundedCornerShape(BowlRadius.sm)).background(color.copy(alpha = 0.65f)))
        }
    }
}
