package com.example.animeapp.ui.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeapp.data.model.Anime
import com.example.animeapp.data.repository.AnimeRepository
import com.example.animeapp.ui.state.AnimeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AnimeViewModel(
    private val repository: AnimeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AnimeUiState>(AnimeUiState.Loading)
    val uiState: StateFlow<AnimeUiState> = _uiState

    private val _favoriteIds = MutableStateFlow<Set<Int>>(emptySet())
    val favoriteIds: StateFlow<Set<Int>> = _favoriteIds

    private val _favoriteList = MutableStateFlow<List<Anime>>(emptyList())
    val favoriteList: StateFlow<List<Anime>> = _favoriteList


    fun loadTopAnime() {
        viewModelScope.launch {
            _uiState.value = AnimeUiState.Loading
            try {
                val animeList = repository.getTopAnime()
                _uiState.value = AnimeUiState.Success(animeList)
            } catch (e: Exception) {
                _uiState.value = AnimeUiState.Error(
                    e.message ?: "Unknown error"
                )
            }
        }
    }

    fun searchAnime(query: String) {
        viewModelScope.launch {
            _uiState.value = AnimeUiState.Loading
            try {
                val animeList = repository.searchAnime(query)
                _uiState.value = AnimeUiState.Success(animeList)
            } catch (e: Exception) {
                _uiState.value = AnimeUiState.Error(
                    e.message ?: "Unknown error"
                )
            }
        }
    }

    fun loadFav(){
        viewModelScope.launch {
            val favAnime = repository.getFavAnime()
            _favoriteList.value = favAnime
            _favoriteIds.value = favAnime.map { it.id }.toSet()
        }
    }

    fun toggleFavorite(anime: Anime) {
        viewModelScope.launch {
            if (_favoriteIds.value.contains(anime.id)) {
                repository.deleteFav(anime.id)
            } else {
                repository.addToFav(anime)
            }
            loadFav()
        }
    }


}
