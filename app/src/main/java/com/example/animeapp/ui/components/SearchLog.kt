package com.example.animeapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.animeapp.ui.state.AnimeUiState
import com.example.animeapp.ui.viewmodel.AnimeViewModel

@Composable
fun SearchLogBottomSheet(
    viewModel: AnimeViewModel,
    onAnimeSelected: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxHeight(0.8f).padding(16.dp)) {
        Text("Log an anime...", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                if (it.length > 2) viewModel.searchAnime(it)
            },
            placeholder = { Text("Search for an anime to review") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (uiState) {
            is AnimeUiState.Loading -> CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            is AnimeUiState.Success -> {
                val list = (uiState as AnimeUiState.Success).animeList
                LazyColumn {
                    items(list) { anime ->
                        ListItem(
                            headlineContent = { Text(anime.title) },
                            supportingContent = { Text(anime.type) },
                            modifier = Modifier.clickable {
                                viewModel.setSelectedAnimeFromApi(anime)
                                onAnimeSelected()
                            }
                        )
                    }
                }
            }
            else -> {}
        }
    }
}