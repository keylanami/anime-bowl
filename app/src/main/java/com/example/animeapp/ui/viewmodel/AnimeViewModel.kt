import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
}
