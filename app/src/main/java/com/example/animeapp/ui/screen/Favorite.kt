package com.example.animeapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.animeapp.ui.components.AnimeItem
import com.example.animeapp.ui.components.EmptyFavView
import com.example.animeapp.ui.viewmodel.AnimeViewModel

@Composable
fun FavoriteScreen(
    viewModel: AnimeViewModel,
    onNavigateToAdd: () -> Unit,
    onNavigateToEdit: (Int) -> Unit
) {
    val favorites by viewModel.favoriteList.collectAsState()

    Scaffold(
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {
                    items(favorites) { anime ->
                        Box(modifier = Modifier.clickable { onNavigateToEdit(anime.id) }) {
                            AnimeItem(
                                anime = anime,
                                isFavorite = true,
                                onFavoriteClick = { }
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}