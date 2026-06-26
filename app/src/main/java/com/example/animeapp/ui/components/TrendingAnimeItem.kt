package com.example.animeapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.animeapp.data.model.Anime
import com.example.animeapp.data.remote.getImageModel
import com.example.animeapp.ui.theme.BowlRadius
import com.example.animeapp.ui.theme.BowlSpacing

@Composable
fun TrendingAnimeItem(
    anime: Anime,
    isReviewed: Boolean = false,
    onLogClick: () -> Unit
) {
    val placeholder = MaterialTheme.colorScheme.surfaceVariant

    Card(
        shape = RoundedCornerShape(BowlRadius.lg),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .padding(BowlSpacing.xs)
    ) {
        Column {
            Box {
                AsyncImage(
                    model = getImageModel(anime.image_url),
                    contentDescription = anime.title,
                    contentScale = ContentScale.Crop,
                    placeholder = ColorPainter(placeholder),
                    error = ColorPainter(placeholder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(184.dp)
                )
                FilledIconButton(
                    onClick = onLogClick,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(BowlSpacing.sm),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Icon(
                        imageVector = if (isReviewed) Icons.Filled.Edit else Icons.Filled.Add,
                        contentDescription = "Log anime",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Column(
                modifier = Modifier.padding(
                    start = BowlSpacing.sm,
                    end = BowlSpacing.sm,
                    top = BowlSpacing.sm,
                    bottom = BowlSpacing.md
                )
            ) {
                Text(
                    text = anime.title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(BowlSpacing.xxs))
                Text(
                    text = "Rating ${anime.score} • ${anime.type.ifBlank { "Anime" }}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
