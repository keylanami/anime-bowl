package com.example.animeapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.animeapp.data.model.Anime
import com.example.animeapp.data.remote.getImageModel
import com.example.animeapp.ui.theme.BowlRadius
import com.example.animeapp.ui.theme.BowlSpacing
import com.example.animeapp.ui.viewmodel.AnimeViewModel

@Composable
fun AnimeItem(
    anime: Anime,
    viewModel: AnimeViewModel,
    onItemClick: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Remove anime?") },
            text = { Text("Remove '${anime.title}' from your diary?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteAnime(anime)
                    showDialog = false
                }) { Text("Remove", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel") }
            }
        )
    }

    Surface(
        shape = RoundedCornerShape(BowlRadius.lg),
        tonalElevation = 1.dp,
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BowlSpacing.md, vertical = BowlSpacing.xs)
            .clickable { onItemClick() }
    ) {
        Row(
            modifier = Modifier.padding(BowlSpacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = getImageModel(anime.image_url),
                contentDescription = anime.title,
                modifier = Modifier
                    .size(width = 72.dp, height = 96.dp)
                    .clip(RoundedCornerShape(BowlRadius.md)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(BowlSpacing.md))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = anime.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(BowlSpacing.xxs))
                Text(
                    text = "${anime.status} • Rating ${anime.score}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = anime.userNote.ifEmpty { "No note yet" },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Box {
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "Menu")
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Remove") },
                        onClick = {
                            showMenu = false
                            showDialog = true
                        }
                    )
                }
            }
        }
    }
}
