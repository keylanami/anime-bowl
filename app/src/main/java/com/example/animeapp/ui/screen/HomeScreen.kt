package com.example.animeapp.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.animeapp.data.model.Anime
import com.example.animeapp.data.remote.FirebaseAuthHelper
import com.example.animeapp.ui.components.AnimeTopBar
import com.example.animeapp.ui.components.LoadingView
import com.example.animeapp.ui.components.TrendingAnimeItem
import com.example.animeapp.ui.state.AnimeUiState
import com.example.animeapp.ui.viewmodel.AnimeViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import com.example.animeapp.R

@Composable
fun HomeScreen(
    viewModel: AnimeViewModel,
    onNavigateToForm: (Anime, Boolean) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val reviewedList by viewModel.favoriteList.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var currentUser by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }

    val allReviews by viewModel.favoriteList.collectAsState()
    val myReviews = allReviews.filter { it.userId == currentUser?.uid }

    LaunchedEffect(Unit) {
        viewModel.loadTopAnime()
    }

    Column(modifier = Modifier.fillMaxSize()) {

        AnimeTopBar()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {

            if (currentUser != null) {


                item {
                    Text(
                        "Your Recent Reviews",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (reviewedList.isEmpty()) {
                    item {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                                    alpha = 0.5f
                                )
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "Kamu belum memberikan review akhir-akhir ini.",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                        HorizontalDivider(
                            Modifier.padding(vertical = 16.dp, horizontal = 16.dp),
                            color = Color(0xFFF0F0F0)
                        )
                    }
                } else {
                    item {
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
                        Divider(
                            Modifier.padding(vertical = 16.dp, horizontal = 16.dp),
                            color = Color(0xFFF0F0F0)
                        )
                    }
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
                                        onLogClick = {
                                            if (currentUser == null) {
                                                scope.launch {
                                                    val user = FirebaseAuthHelper.signInWithGoogle(
                                                        context,
                                                        context.getString(R.string.default_web_client_id)
                                                    )
                                                    if (user != null) {
                                                        currentUser = user
                                                        onNavigateToForm(anime, false)
                                                    }
                                                }
                                            } else {
                                                onNavigateToForm(anime, false)
                                            }
                                        }
                                    )
                                }
                            }
                            if (rowItems.size < 2) Spacer(Modifier.weight(1f))
                        }
                    }
                }

                is AnimeUiState.Error -> item {
                    val errorMessage = (uiState as AnimeUiState.Error).message
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.loadTopAnime() }) {
                            Icon(Icons.Filled.Refresh, contentDescription = "Retry")
                            Spacer(Modifier.width(8.dp))
                            Text("Coba Lagi")
                        }
                    }
                }
            }
        }
    }
}