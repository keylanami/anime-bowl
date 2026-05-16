package com.example.animeapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.animeapp.R
import com.example.animeapp.data.local.pref.UserPreferences
import com.example.animeapp.ui.components.LogAnimeItem
import com.example.animeapp.ui.viewmodel.AnimeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: AnimeViewModel,
    onNavigateToEdit: (Int) -> Unit,
    onNavigateToTrash: () -> Unit
) {
    val logs by viewModel.favoriteList.collectAsState()
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val isGridMode by userPreferences.isGridMode.collectAsState(initial = false)
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Anime Bowl") },
                actions = {
                    IconButton(onClick = { scope.launch { userPreferences.toggleGridMode() } }) {
                        Icon(

                            painter = if (isGridMode) {
                                painterResource(R.drawable.outline_grid_view_24)
                            } else {
                                painterResource(R.drawable.outline_grid_view_24)
                            },
                            contentDescription = "Toggle Layout"
                        )
                    }
                    IconButton(onClick = onNavigateToTrash) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = "Recycle Bin",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (logs.isEmpty()) {
                // EMPTY STATE PROFILE: Ikon + Teks ala Jetbrains
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_folder_open_24),
                        contentDescription = null,
                        modifier = Modifier.size(72.dp),
                        tint = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Belum ada log review anime.",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Ketuk ikon cari di bawah untuk menambah.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            } else {
                if (isGridMode) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 100.dp, start = 8.dp, end = 8.dp)
                    ) {
                        items(logs) { anime ->
                            LogAnimeItem(
                                anime = anime,
                                onEdit = { onNavigateToEdit(anime.id) },
                                onDelete = { viewModel.moveToTrash(anime) }
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 100.dp, start = 16.dp, end = 16.dp)
                    ) {
                        items(logs) { anime ->
                            LogAnimeItem(
                                anime = anime,
                                onEdit = { onNavigateToEdit(anime.id) },
                                onDelete = { viewModel.moveToTrash(anime) }
                            )
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}
