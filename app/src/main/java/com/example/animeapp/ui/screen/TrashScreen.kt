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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.animeapp.data.model.Anime
import com.example.animeapp.data.remote.FirestoreHelper
import com.example.animeapp.data.remote.getImageModel
import com.example.animeapp.ui.components.EmptyFavView
import com.example.animeapp.ui.theme.BowlRadius
import com.example.animeapp.ui.theme.BowlSpacing
import com.example.animeapp.ui.viewmodel.AnimeViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrashScreen(
    viewModel: AnimeViewModel,
    onNavigateUp: () -> Unit
) {
    val trashedItems by viewModel.trashedList.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var animeToDelete by remember { mutableStateOf<Anime?>(null) }

    val scope = rememberCoroutineScope()
    val currentUser = FirebaseAuth.getInstance().currentUser

    if (showDialog && animeToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            title = {
                Text(
                    "Hapus Permanen?",
                    fontWeight = FontWeight.SemiBold
                )
            },
            text = {
                Text("Data '${animeToDelete?.title}' akan dihapus selamanya. Tindakan ini tidak bisa dibatalkan.")
            },
            confirmButton = {
                TextButton(onClick = {
                    animeToDelete?.let { anime ->
                        viewModel.deleteAnime(anime)

                        if (currentUser != null) {
                            scope.launch {
                                FirestoreHelper.deleteReviewFromServer(currentUser.uid, anime.mal_id)
                            }
                        }
                    }
                    showDialog = false
                }) { Text("Hapus", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                    }) {
                    Text("Batal")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Recycle Bin",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
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
            if (trashedItems.isEmpty()) {
                EmptyFavView(
                    title = "Recycle bin is empty",
                    message = "Deleted review logs will appear here before permanent removal."
                )
            } else {
                LazyColumn(
                    Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 24.dp, top = BowlSpacing.xs)
                ) {
                    items(trashedItems) { anime ->
                        Card(
                            shape = RoundedCornerShape(BowlRadius.lg),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = BowlSpacing.md, vertical = BowlSpacing.xs)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(BowlSpacing.sm)
                            ) {
                                AsyncImage(
                                    model = getImageModel( anime.image_url),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(width = 58.dp, height = 78.dp)
                                        .clip(RoundedCornerShape(BowlRadius.md)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(Modifier.width(BowlSpacing.md))
                                Column(Modifier.weight(1f)) {
                                    Text(
                                        anime.title,
                                        maxLines = 1,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        "Deleted",
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                IconButton(onClick = { viewModel.restoreFromTrash(anime) }) {
                                    Icon(Icons.Filled.Refresh, contentDescription = "Restore")
                                }
                                IconButton(onClick = {
                                    animeToDelete = anime
                                    showDialog = true
                                }) {
                                    Icon(
                                        Icons.Filled.Delete,
                                        contentDescription = "Delete Permanent",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
