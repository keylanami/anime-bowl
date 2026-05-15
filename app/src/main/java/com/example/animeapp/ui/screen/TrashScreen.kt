package com.example.animeapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.animeapp.data.model.Anime
import com.example.animeapp.ui.viewmodel.AnimeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrashScreen(
    viewModel: AnimeViewModel,
    onNavigateUp: () -> Unit
) {
    val trashedItems by viewModel.trashedList.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var animeToDelete by remember { mutableStateOf<Anime?>(null) }

    if (showDialog && animeToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Hapus Permanen?") },
            text = { Text("Data '${animeToDelete?.title}' akan dihapus selamanya dari database. Kamu yakin?") },
            confirmButton = {
                TextButton(onClick = {
                    animeToDelete?.let { viewModel.deleteAnime(it) }
                    showDialog = false
                }) { Text("Hapus") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Batal") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recycle Bin") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(Modifier
            .padding(paddingValues)
            .fillMaxSize()) {
            items(trashedItems) { anime ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        AsyncImage(
                            model = anime.image_url.ifEmpty { "https://via.placeholder.com/150" },
                            contentDescription = null,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(
                                anime.title,
                                maxLines = 1,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                "Dihapus",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        IconButton(onClick = { viewModel.restoreFromTrash(anime.id) }) {
                            Icon(Icons.Filled.Refresh, contentDescription = "Restore")
                        }

                        IconButton(onClick = {
                            animeToDelete = anime
                            showDialog = true
                        }) {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = "Delete Permanent",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}