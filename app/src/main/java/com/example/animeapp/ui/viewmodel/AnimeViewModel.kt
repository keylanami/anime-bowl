package com.example.animeapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeapp.data.model.Anime
import com.example.animeapp.data.repository.AnimeRepository
import com.example.animeapp.ui.state.AnimeUiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException

@OptIn(FlowPreview::class)
class AnimeViewModel(
    private val repository: AnimeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AnimeUiState>(AnimeUiState.Initial)
    val uiState: StateFlow<AnimeUiState> = _uiState

    private val _searchUiState = MutableStateFlow<AnimeUiState>(AnimeUiState.Initial)
    val searchUiState: StateFlow<AnimeUiState> = _searchUiState

    private val searchQuery = MutableStateFlow("")

    init {
        viewModelScope.launch {
            searchQuery
                .map { it.trim() }
                .debounce(350)
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.length < 3) {
                        _searchUiState.value = AnimeUiState.Initial
                        return@collectLatest
                    }

                    _searchUiState.value = AnimeUiState.Loading
                    try {
                        val animeList = repository.searchAnime(query)
                        _searchUiState.value = if (animeList.isEmpty()) {
                            AnimeUiState.Empty
                        } else {
                            AnimeUiState.Success(animeList)
                        }
                    } catch (e: IOException) {
                        _searchUiState.value = AnimeUiState.Error("Tidak ada koneksi internet. Perika jaringan Anda.")
                    } catch (e: Exception) {
                        _searchUiState.value = AnimeUiState.Error(e.message ?: "Pencarian gagal")
                    }
                }
        }
    }

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
                _uiState.value = if (animeList.isEmpty()) AnimeUiState.Empty else AnimeUiState.Success(animeList)
            } catch (e: IOException){
                _uiState.value = AnimeUiState.Error("Tidak ada koneksi internet. Perika jaringan Anda.")
            }

            catch (e: Exception) {
                _uiState.value = AnimeUiState.Error(e.message ?: "Gagal memuat data")
            }
        }
    }

    fun searchAnime(query: String) {
        searchQuery.value = query
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

    private val _selectedAnime = MutableStateFlow<Anime?>(null)
    val selectedAnime: StateFlow<Anime?> = _selectedAnime

    fun getAnimeById(id: Int) {
        viewModelScope.launch {
            _selectedAnime.value = repository.getAnimeById(id)
        }
    }

    fun clearSelectedAnime() {
        _selectedAnime.value = null
    }

    val trashedList: StateFlow<List<Anime>> = repository.getTrashedAnime()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    fun moveToTrash(anime: Anime) = viewModelScope.launch {
        repository.updateAnime(anime.copy(is_in_trash = true))
    }

    fun restoreFromTrash(anime: Anime) = viewModelScope.launch {
        repository.updateAnime(anime.copy(is_in_trash = false))
    }

    fun setSelectedAnimeFromApi(anime: Anime) {
        _selectedAnime.value = anime
    }

}
