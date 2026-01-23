package com.example.animeapp.ui.state

import com.example.animeapp.data.model.Anime

sealed class AnimeUiState{
    object Loading: AnimeUiState()
    data class Success(val animeList: List<Anime>): AnimeUiState()
    data class Error(val message: String): AnimeUiState()
}