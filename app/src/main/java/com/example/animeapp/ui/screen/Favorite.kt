
package com.example.animeapp.ui.screen



import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.animeapp.ui.components.AnimeItem
import com.example.animeapp.ui.components.AnimeList
import com.example.animeapp.ui.components.EmptyFavView
import com.example.animeapp.ui.viewmodel.AnimeViewModel

@Composable
fun FavoriteScreen(viewModel: AnimeViewModel) {
    val favorites by viewModel.favoriteList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadFav()
    }

    if (favorites.isEmpty()) {
        EmptyFavView()
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            items(favorites) { anime ->
                AnimeItem(
                    anime = anime,
                    isFavorite = true,
                    onFavoriteClick = { viewModel.toggleFavorite(anime) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}
