package com.example.animeapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.animeapp.R
import com.example.animeapp.data.model.Anime
import com.example.animeapp.data.remote.FirebaseAuthHelper
import com.example.animeapp.ui.components.AnimeTopBar
import com.example.animeapp.ui.components.EmptyFavView
import com.example.animeapp.ui.components.ErrorView
import com.example.animeapp.ui.components.LoadingView
import com.example.animeapp.ui.components.TrendingAnimeItem
import com.example.animeapp.ui.state.AnimeUiState
import com.example.animeapp.ui.theme.BowlRadius
import com.example.animeapp.ui.theme.BowlSpacing
import com.example.animeapp.ui.viewmodel.AnimeViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: AnimeViewModel,
    onNavigateToForm: (Anime, Boolean) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val allReviews by viewModel.favoriteList.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var currentUser by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }

    DisposableEffect(Unit) {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            currentUser = auth.currentUser
        }
        FirebaseAuth.getInstance().addAuthStateListener(listener)
        onDispose { FirebaseAuth.getInstance().removeAuthStateListener(listener) }
    }

    val myReviews = remember(allReviews, currentUser) {
        allReviews.filter { it.userId == currentUser?.uid }
    }

    LaunchedEffect(Unit) {
        viewModel.loadTopAnime()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = BowlSpacing.xs)
    ) {
        AnimeTopBar()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 112.dp)
        ) {
            item {
                HomeHero(
                    reviewCount = myReviews.size,
                    isSignedIn = currentUser != null
                )
            }

            if (currentUser != null) {
                item {
                    SectionHeader(
                        title = "Recent reviews",
                        subtitle = "A quick path back to what you have been watching."
                    )
                }
                if (myReviews.isEmpty()) {
                    item {
                        CompactEmptyCard(
                            title = "No reviews yet",
                            message = "Use search below to add your first anime log."
                        )
                    }
                } else {
                    item {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = BowlSpacing.md),
                            horizontalArrangement = Arrangement.spacedBy(BowlSpacing.xs)
                        ) {
                            items(myReviews.take(8)) { anime ->
                                Box(modifier = Modifier.width(150.dp)) {
                                    TrendingAnimeItem(
                                        anime = anime,
                                        isReviewed = true,
                                        onLogClick = { onNavigateToForm(anime, true) }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                SectionHeader(
                    title = "Trending now",
                    subtitle = "Popular anime worth adding to your bowl."
                )
            }

            when (uiState) {
                is AnimeUiState.Loading -> item { LoadingView() }
                is AnimeUiState.Success -> {
                    val animeList = (uiState as AnimeUiState.Success).animeList
                    val chunks = animeList.chunked(2)
                    items(chunks) { rowItems ->
                        Row(
                            Modifier.padding(horizontal = BowlSpacing.xs),
                            horizontalArrangement = Arrangement.spacedBy(BowlSpacing.xs)
                        ) {
                            rowItems.forEach { anime ->
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
                    ErrorView(
                        message = (uiState as AnimeUiState.Error).message,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(BowlSpacing.xl),
                        onRetry = { viewModel.loadTopAnime() }
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeHero(reviewCount: Int, isSignedIn: Boolean) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BowlSpacing.md, vertical = BowlSpacing.xs),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(BowlRadius.xl),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f)
    ) {
        Row(
            modifier = Modifier.padding(BowlSpacing.lg),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(BowlSpacing.md)
        ) {
            Icon(
                imageVector = Icons.Outlined.AutoAwesome,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Column {
                Text(
                    text = if (isSignedIn) "$reviewCount reviews in your bowl" else "Curate your anime bowl",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(BowlSpacing.xxs))
                Text(
                    text = if (isSignedIn) "Browse trending picks or refine a recent review."
                    else "Sign in when saving a review to keep your list synced.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, subtitle: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = BowlSpacing.lg,
                end = BowlSpacing.lg,
                top = BowlSpacing.xl,
                bottom = BowlSpacing.sm
            )
    ) {
        Text(title, style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(BowlSpacing.xxs))
        Text(
            subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun CompactEmptyCard(title: String, message: String) {
    Box(
        modifier = Modifier
            .height(180.dp)
            .padding(horizontal = BowlSpacing.md)
    ) {
        EmptyFavView(title = title, message = message)
    }
}
