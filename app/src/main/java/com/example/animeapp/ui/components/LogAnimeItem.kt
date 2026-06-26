package com.example.animeapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Notes
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.animeapp.data.model.Anime
import com.example.animeapp.data.remote.getImageModel
import com.example.animeapp.ui.theme.BowlRadius
import com.example.animeapp.ui.theme.BowlSpacing

@Composable
fun LogAnimeItem(anime: Anime, onEdit: () -> Unit, onDelete: () -> Unit) {
    val placeholder = MaterialTheme.colorScheme.surfaceVariant

    Card(
        shape = RoundedCornerShape(BowlRadius.lg),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = BowlSpacing.xs)
    ) {
        Column {
            AsyncImage(
                model = getImageModel(anime.image_url),
                contentDescription = anime.title,
                contentScale = ContentScale.Crop,
                placeholder = ColorPainter(placeholder),
                error = ColorPainter(placeholder),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(172.dp)
            )
            Column(Modifier.padding(BowlSpacing.md)) {
                Text(
                    text = anime.title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(BowlSpacing.xxs))
                Text(
                    text = "Rating ${anime.score} • ${anime.status}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (anime.userNote.isNotBlank()) {
                    Spacer(modifier = Modifier.height(BowlSpacing.sm))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(BowlSpacing.xs),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Notes,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = anime.userNote,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontStyle = FontStyle.Italic,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(BowlSpacing.sm))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onEdit) {
                        Icon(
                            Icons.Outlined.Edit,
                            contentDescription = "Edit",
                            Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(BowlSpacing.xxs))
                        Text("Edit")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Outlined.Delete,
                            contentDescription = "Delete",
                            Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}
