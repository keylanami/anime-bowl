package com.example.animeapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.animeapp.data.local.pref.UserPreferences
import com.example.animeapp.ui.components.AnimeItem
import com.example.animeapp.ui.components.EmptyFavView
import com.example.animeapp.ui.viewmodel.AnimeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    viewModel: AnimeViewModel,
    onNavigateToAdd: () -> Unit,
    onNavigateToEdit: (Int) -> Unit,
    onNavigateToSearch: () -> Unit
) {
    val favorites by viewModel.favoriteList.collectAsState()
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val isGridMode by userPreferences.isGridMode.collectAsState(initial = false)
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Diary") },
                actions = {
                    IconButton(onClick = { coroutineScope.launch { userPreferences.toggleGridMode() } }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Toggle View")
                    }
                    IconButton(onClick = onNavigateToSearch) {
                        Icon(Icons.Filled.Search, contentDescription = "Search API")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAdd) {
                Icon(Icons.Filled.Add, contentDescription = "Tambah Anime")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (favorites.isEmpty()) {
                EmptyFavView()
            } else {
                if (isGridMode) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 80.dp, start = 8.dp, end = 8.dp, top = 8.dp)
                    ) {
                        items(favorites) { anime ->
                            // Hapus bungkus Box, oper navigasi langsung ke dalam item
                            AnimeItem(
                                anime = anime,
                                viewModel = viewModel,
                                onItemClick = { onNavigateToEdit(anime.id) }
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 80.dp, start = 8.dp, end = 8.dp, top = 8.dp)
                    ) {
                        items(favorites) { anime ->
                            AnimeItem(
                                anime = anime,
                                viewModel = viewModel,
                                onItemClick = { onNavigateToEdit(anime.id) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}