package com.example.animeapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.animeapp.data.model.Anime
import com.example.animeapp.ui.viewmodel.AnimeViewModel

@Composable
fun AnimeItem(
    anime: Anime,
    viewModel: AnimeViewModel,
    onItemClick: () -> Unit // Sisakan ini untuk navigasi form
) {
    var showMenu by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Hapus Anime?") },
            text = { Text("Apakah kamu yakin ingin menghapus '${anime.title}' dari diary?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteAnime(anime)
                    showDialog = false
                }) { Text("Hapus") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Batal") }
            }
        )
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 5.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .clickable { onItemClick() } // ADD THIS: Supaya item bisa diklik untuk edit
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = anime.image_url.ifEmpty { "https://via.placeholder.com/150" },
                contentDescription = anime.title,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = anime.title, style = MaterialTheme.typography.titleMedium, maxLines = 1)
                Text(text = "Status: ${anime.status}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Rating: ⭐ ${anime.score}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Catatan: ${anime.userNote.ifEmpty { "-" }}",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2
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
                        text = { Text("Hapus Data") },
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