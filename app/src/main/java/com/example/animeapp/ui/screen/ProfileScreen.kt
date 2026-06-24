package com.example.animeapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.animeapp.R
import com.example.animeapp.data.local.pref.UserPreferences
import com.example.animeapp.ui.components.LogAnimeItem
import com.example.animeapp.ui.viewmodel.AnimeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.animeapp.data.remote.FirebaseAuthHelper
import com.example.animeapp.ui.components.ProfileDialog
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

    val filterOptions = listOf("All", "Plan to Watch", "Watching", "Completed")
    var selectedFilter by remember { mutableStateOf("All") }

    var currentUser by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }
    var showProfileDialog by remember { mutableStateOf(false) }


    val filteredLogs = remember(logs, selectedFilter) {
        if (selectedFilter == "All") logs
        else logs.filter { it.status == selectedFilter }
    }

    if (showProfileDialog && currentUser != null) {
        ProfileDialog(
            user = currentUser!!,
            onDismissRequest = { showProfileDialog = false },
            onConfirmation = {
                scope.launch {
                    FirebaseAuthHelper.signOut(context)
                    currentUser = null
                    showProfileDialog = false
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Anime Bowl", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { scope.launch { userPreferences.toggleGridMode() } }) {
                        Icon(
                            painter = if (isGridMode) painterResource(R.drawable.outline_grid_view_24)
                            else painterResource(R.drawable.outline_lists_24),
                            contentDescription = "Toggle Layout"
                        )
                    }
                    IconButton(onClick = onNavigateToTrash) {
                        Icon(Icons.Filled.Delete, contentDescription = "Recycle Bin", tint = MaterialTheme.colorScheme.error)
                    }

                    IconButton(onClick = {
                        if (currentUser == null) {
                            scope.launch {
                                val user = FirebaseAuthHelper.signInWithGoogle(
                                    context,
                                    context.getString(R.string.default_web_client_id)
                                )
                                if (user != null) currentUser = user
                            }
                        } else {
                            showProfileDialog = true
                        }
                    }) {
                        if (currentUser?.photoUrl != null) {
                            AsyncImage(
                                model = currentUser?.photoUrl,
                                contentDescription = "Profile",
                                modifier = Modifier.size(28.dp).clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(Icons.Filled.AccountCircle, contentDescription = "Login", modifier = Modifier.size(28.dp))
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (logs.isNotEmpty()) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filterOptions) { filter ->
                        FilterChip(
                            selected = selectedFilter == filter,
                            onClick = { selectedFilter = filter },
                            label = { Text(filter) }
                        )
                    }
                }
            }

            if (logs.isEmpty()) {
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
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Ketuk ikon cari di bawah untuk menambah.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            } else if (filteredLogs.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Tidak ada anime di kategori ini.",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
            } else {
                if (isGridMode) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            bottom = 100.dp,
                            start = 8.dp,
                            end = 8.dp,
                            top = 8.dp
                        )
                    ) {
                        items(filteredLogs) { anime ->
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
                        contentPadding = PaddingValues(
                            bottom = 100.dp,
                            start = 16.dp,
                            end = 16.dp,
                            top = 8.dp
                        )
                    ) {
                        items(filteredLogs) { anime ->
                            LogAnimeItem(
                                anime = anime,
                                onEdit = { onNavigateToEdit(anime.id) },
                                onDelete = { viewModel.moveToTrash(anime) }
                            )
                            Spacer(Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }
}