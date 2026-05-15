package com.example.animeapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.animeapp.ui.viewmodel.AnimeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrashScreen(viewModel: AnimeViewModel) {
    val trashedItems by viewModel.trashedList.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Recycle Bin") }) }
    ) { paddingValues ->
        LazyColumn(Modifier.padding(paddingValues).fillMaxSize()) {
            items(trashedItems) { anime ->
                ListItem(
                    headlineContent = { Text(anime.title) },
                    supportingContent = { Text("Reason: Trashed") },
                    trailingContent = {
                        Row {
                            IconButton(onClick = { viewModel.restoreFromTrash(anime.id) }) {
                                Icon(Icons.Filled.Refresh, contentDescription = "Restore")
                            }
                            IconButton(onClick = { viewModel.deleteAnime(anime) }) {
                                Icon(Icons.Filled.DeleteForever, contentDescription = "Delete Permanent")
                            }
                        }
                    }
                )
            }
        }
    }
}