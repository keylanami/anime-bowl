package com.example.animeapp.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.example.animeapp.data.model.Anime
import com.example.animeapp.ui.viewmodel.AnimeViewModel

@Composable
fun AnimeList(
    animeList: List<Anime>,
    viewModel: AnimeViewModel,
    onItemClick: (Int) -> Unit
) {
    LazyColumn {
        items(animeList) { anime ->
            AnimeItem(
                anime = anime,
                viewModel = viewModel,
                onItemClick = { onItemClick(anime.id) }
            )
        }
    }
}