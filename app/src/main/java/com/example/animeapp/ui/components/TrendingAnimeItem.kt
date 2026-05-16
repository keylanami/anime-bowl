package com.example.animeapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.animeapp.data.model.Anime

@Composable
fun TrendingAnimeItem(
    anime: Anime,
    isReviewed: Boolean = false,
    onLogClick: () -> Unit
) {
    val lightPlaceholder = Color(0xFFF5F5F5)

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        Column {
            Box {
                AsyncImage(
                    model = anime.image_url,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    placeholder = ColorPainter(lightPlaceholder),
                    error = ColorPainter(lightPlaceholder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
                SmallFloatingActionButton(
                    onClick = onLogClick,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Icon(
                        imageVector = if (isReviewed) Icons.Filled.Edit else Icons.Filled.Add,
                        contentDescription = "Log",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = anime.title,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "⭐ ${anime.score}",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray // Kontras rating
                )
            }
        }
    }
}