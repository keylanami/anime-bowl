package com.example.animeapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.animeapp.data.model.Anime
import com.example.animeapp.ui.components.LoadingView
import com.example.animeapp.ui.components.TrendingAnimeItem
import com.example.animeapp.ui.state.AnimeUiState
import com.example.animeapp.ui.viewmodel.AnimeViewModel

@Composable
fun HomeScreen(
    viewModel: AnimeViewModel,
    onNavigateToForm: (Anime, Boolean) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val reviewedList by viewModel.favoriteList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadTopAnime()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        if (reviewedList.isNotEmpty()) {
            item {
                Text(
                    "Your Recent Reviews",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(reviewedList) { anime ->
                        Box(modifier = Modifier.width(120.dp)) {
                            TrendingAnimeItem(
                                anime = anime,
                                isReviewed = true,
                                onLogClick = { onNavigateToForm(anime, true) }
                            )
                        }
                    }
                }
                HorizontalDivider(Modifier.padding(vertical = 16.dp, horizontal = 16.dp))
            }
        }

        item {
            Text(
                "Discover Trending",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        when (uiState) {
            is AnimeUiState.Loading -> item { LoadingView() }
            is AnimeUiState.Success -> {
                val animeList = (uiState as AnimeUiState.Success).animeList
                val chunks = animeList.chunked(2)
                items(chunks) { rowItems ->
                    Row(Modifier.padding(horizontal = 8.dp)) {
                        for (anime in rowItems) {
                            Box(Modifier.weight(1f)) {
                                TrendingAnimeItem(
                                    anime = anime,
                                    onLogClick = { onNavigateToForm(anime, false) }
                                )
                            }
                        }
                        if (rowItems.size < 2) Spacer(Modifier.weight(1f))
                    }
                }
            }

            is AnimeUiState.Error -> item { Text("Error loading data") }
        }
    }
}