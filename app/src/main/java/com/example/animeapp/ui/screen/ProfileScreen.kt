package com.example.animeapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.animeapp.data.local.UserPreferences
import com.example.animeapp.ui.components.LogAnimeItem
import com.example.animeapp.ui.viewmodel.AnimeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: AnimeViewModel,
    onNavigateToEdit: (Int) -> Unit,
    onNavigateToTrash: () -> Unit
) {
    val logs by viewModel.favoriteList.collectAsState()
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val isGridMode by userPreferences.isGridMode.collectAsState(initial = false)
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Anime Logs") },
                actions = {
                    IconButton(onClick = { scope.launch { userPreferences.toggleGridMode() } }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Toggle Layout")
                    }
                    IconButton(onClick = onNavigateToTrash) {
                        Icon(Icons.Filled.Delete, contentDescription = "Recycle Bin", tint = MaterialTheme.colorScheme.error)
                    }
                }
            )
        }
    ) { paddingValues ->
        if (logs.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("No reviews yet. Start logging!")
            }
        } else {
            if (isGridMode) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentPadding = PaddingValues(bottom = 100.dp, start = 8.dp, end = 8.dp)
                ) {
                    items(logs) { anime ->
                        LogAnimeItem(anime, onEdit = { onNavigateToEdit(anime.id) }, onDelete = { viewModel.moveToTrash(anime.id) })
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentPadding = PaddingValues(bottom = 100.dp, start = 16.dp, end = 16.dp)
                ) {
                    items(logs) { anime ->
                        LogAnimeItem(anime, onEdit = { onNavigateToEdit(anime.id) }, onDelete = { viewModel.moveToTrash(anime.id) })
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}