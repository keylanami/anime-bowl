package com.example.animeapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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

    Column(modifier = Modifier
        .fillMaxHeight(0.8f)
        .padding(16.dp)) {
        Text(
            text = "Log an anime...",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                if (it.length > 2) viewModel.searchAnime(it)
            },
            placeholder = {
                Text("Search for an anime to review", color = Color.Gray)
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (uiState) {
            is AnimeUiState.Loading -> CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            is AnimeUiState.Success -> {
                val list = (uiState as AnimeUiState.Success).animeList
                LazyColumn {
                    items(list) { anime ->
                        ListItem(
                            headlineContent = {
                                Text(
                                    text = anime.title,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold
                                )
                            },
                            supportingContent = {
                                Text(
                                    text = anime.type,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )
                            },
                            modifier = Modifier.clickable {
                                viewModel.setSelectedAnimeFromApi(anime)
                                onAnimeSelected()
                            }
                        )
                        HorizontalDivider(color = Color(0xFFF0F0F0))
                    }
                }
            }

            else -> {}
        }
    }
}