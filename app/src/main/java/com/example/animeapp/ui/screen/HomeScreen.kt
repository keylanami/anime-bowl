package com.example.animeapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    viewModel: AnimeViewModel, onLogAnime: (Anime) -> Unit
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

                        Box(modifier = Modifier.width(100.dp)) {
                            TrendingAnimeItem(anime = anime, onLogClick = { onLogAnime(anime) })
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
                                TrendingAnimeItem(anime = anime, onLogClick = { onLogAnime(anime) })
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