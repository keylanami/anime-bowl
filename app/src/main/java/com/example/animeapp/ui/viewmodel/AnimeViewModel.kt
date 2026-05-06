package com.example.animeapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeapp.data.model.Anime
import com.example.animeapp.data.repository.AnimeRepository
import com.example.animeapp.ui.state.AnimeUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AnimeViewModel(
    private val repository: AnimeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AnimeUiState>(AnimeUiState.Loading)
    val uiState: StateFlow<AnimeUiState> = _uiState

    val favoriteList: StateFlow<List<Anime>> = repository.getFavAnime()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun loadTopAnime() {
        viewModelScope.launch {
            _uiState.value = AnimeUiState.Loading
            try {
                val animeList = repository.getTopAnime()
                _uiState.value = AnimeUiState.Success(animeList)
            } catch (e: Exception) {
                _uiState.value = AnimeUiState.Error(e.message ?: "Gagal memuat data")
            }
        }
    }

    fun searchAnime(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch {
            _uiState.value = AnimeUiState.Loading
            try {
                val animeList = repository.searchAnime(query)
                _uiState.value = AnimeUiState.Success(animeList)
            } catch (e: Exception) {
                _uiState.value = AnimeUiState.Error(e.message ?: "Pencarian gagal")
            }
        }
    }

    fun insertAnime(anime: Anime) = viewModelScope.launch {
        repository.insertAnime(anime)
    }

    fun updateAnime(anime: Anime) = viewModelScope.launch {
        repository.updateAnime(anime)
    }

    fun deleteAnime(anime: Anime) = viewModelScope.launch {
        repository.deleteAnime(anime)
    }

    fun toggleFavoriteFromApi(anime: Anime) {
        viewModelScope.launch {
            val exists = repository.isFavorite(anime.mal_id ?: 0)
            if (exists) {
                repository.deleteByMalId(anime.mal_id ?: 0)
            } else {
                repository.insertAnime(anime)
            }
        }
    }
}