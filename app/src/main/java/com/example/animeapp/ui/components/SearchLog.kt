package com.example.animeapp.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.animeapp.data.remote.getImageModel
import com.example.animeapp.ui.state.AnimeUiState
import com.example.animeapp.ui.theme.BowlRadius
import com.example.animeapp.ui.theme.BowlSpacing
import com.example.animeapp.ui.theme.bowlTextFieldColors
import com.example.animeapp.ui.viewmodel.AnimeViewModel

@Composable
fun SearchLogBottomSheet(
    viewModel: AnimeViewModel,
    onAnimeSelected: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    val recentSearches = remember { mutableStateListOf<String>() }
    val searchUiState by viewModel.searchUiState.collectAsState()
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxHeight(0.86f)
            .padding(horizontal = BowlSpacing.lg, vertical = BowlSpacing.md)
    ) {
        Text(
            text = "Add to Bowl",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(BowlSpacing.sm))

        OutlinedTextField(
            value = query,
            onValueChange = { nextQuery ->
                query = nextQuery
                viewModel.searchAnime(nextQuery)
                if (nextQuery.length > 2 &&
                    recentSearches.none { recent -> recent.equals(nextQuery, ignoreCase = true) }
                ) {
                    recentSearches.add(0, nextQuery)
                    if (recentSearches.size > 4) recentSearches.removeLast()
                }
            },
            leadingIcon = {
                Icon(Icons.Outlined.Search, contentDescription = null)
            },
            trailingIcon = {
                AnimatedVisibility(visible = query.isNotBlank()) {
                    IconButton(
                        onClick = {
                            query = ""
                            viewModel.searchAnime("")
                        }
                    ) {
                        Icon(Icons.Outlined.Close, contentDescription = "Clear search")
                    }
                }
            },
            placeholder = {
                Text("Search anime title")
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            singleLine = true,
            shape = RoundedCornerShape(BowlRadius.lg),
            colors = bowlTextFieldColors()
        )

        Spacer(modifier = Modifier.height(BowlSpacing.md))

        val displayState = if (query.length <= 2) AnimeUiState.Initial else searchUiState
        AnimatedContent(
            targetState = displayState,
            label = "searchState"
        ) { state ->
            when (state) {
                AnimeUiState.Initial -> SearchIdleState(
                    recentSearches = recentSearches,
                    onSearchClick = {
                        query = it
                        viewModel.searchAnime(it)
                    }
                )
                AnimeUiState.Loading -> Column(verticalArrangement = Arrangement.spacedBy(BowlSpacing.sm)) {
                    repeat(4) { LoadingCardPlaceholder() }
                }
                AnimeUiState.Empty -> EmptyInlineState("No results", "Try a different title or spelling.")
                is AnimeUiState.Success -> SearchResults(
                    results = state.animeList,
                    onAnimeSelected = { anime ->
                        viewModel.setSelectedAnimeFromApi(anime)
                        onAnimeSelected()
                    }
                )
                is AnimeUiState.Error -> ErrorView(
                    message = state.message,
                    modifier = Modifier.fillMaxWidth(),
                    onRetry = { if (query.length > 2) viewModel.searchAnime(query) }
                )
            }
        }
    }
}

@Composable
private fun SearchResults(
    results: List<com.example.animeapp.data.model.Anime>,
    onAnimeSelected: (com.example.animeapp.data.model.Anime) -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(BowlSpacing.sm)) {
        items(results) { anime ->
            SearchResultRow(
                title = anime.title,
                metadata = "${anime.type.ifBlank { "Anime" }} • Rating ${anime.score}",
                image = anime.image_url,
                onClick = { onAnimeSelected(anime) }
            )
        }
    }
}

@Composable
private fun SearchIdleState(
    recentSearches: List<String>,
    onSearchClick: (String) -> Unit
) {
    if (recentSearches.isEmpty()) {
        EmptyInlineState(
            title = "Find your next review",
            message = "Type at least three characters to search the anime catalog."
        )
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(BowlSpacing.xs)) {
            Text(
                "Recent searches",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            recentSearches.forEach { recent ->
                ListItem(
                    headlineContent = { Text(recent) },
                    leadingContent = {
                        Icon(Icons.Outlined.History, contentDescription = null)
                    },
                    modifier = Modifier.clickable { onSearchClick(recent) }
                )
            }
        }
    }
}

@Composable
private fun SearchResultRow(
    title: String,
    metadata: String,
    image: String,
    onClick: () -> Unit
) {
    val placeholder = MaterialTheme.colorScheme.surfaceVariant
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(BowlRadius.lg))
            .clickable(onClick = onClick)
            .padding(BowlSpacing.xs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = getImageModel(image),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            placeholder = ColorPainter(placeholder),
            error = ColorPainter(placeholder),
            modifier = Modifier
                .size(width = 56.dp, height = 76.dp)
                .clip(RoundedCornerShape(BowlRadius.md))
        )
        Spacer(Modifier.width(BowlSpacing.md))
        Column(Modifier.weight(1f)) {
            Text(
                title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(BowlSpacing.xxs))
            Text(
                metadata,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun EmptyInlineState(title: String, message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = BowlSpacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BowlSpacing.xs)
    ) {
        Icon(
            Icons.Outlined.Search,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(36.dp)
        )
        Text(title, style = MaterialTheme.typography.titleMedium)
        Text(
            message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
