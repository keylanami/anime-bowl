package com.example.animeapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.animeapp.R
import com.example.animeapp.data.local.pref.UserPreferences
import com.example.animeapp.data.remote.FirebaseAuthHelper
import com.example.animeapp.ui.components.EmptyFavView
import com.example.animeapp.ui.components.LogAnimeItem
import com.example.animeapp.ui.components.ProfileDialog
import com.example.animeapp.ui.theme.BowlRadius
import com.example.animeapp.ui.theme.BowlSpacing
import com.example.animeapp.ui.theme.bowlChipColors
import com.example.animeapp.ui.viewmodel.AnimeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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

    val myLogs = remember(logs, currentUser) {
        logs.filter { it.userId == currentUser?.uid }
    }
    val filteredLogs = remember(myLogs, selectedFilter) {
        if (selectedFilter == "All") myLogs else myLogs.filter { it.status == selectedFilter }
    }

    DisposableEffect(Unit) {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            currentUser = auth.currentUser
        }
        FirebaseAuth.getInstance().addAuthStateListener(listener)
        onDispose { FirebaseAuth.getInstance().removeAuthStateListener(listener) }
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
                title = { Text("Profile", fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                actions = {
                    IconButton(onClick = { scope.launch { userPreferences.toggleGridMode() } }) {
                        Icon(
                            imageVector = if (isGridMode) Icons.Outlined.GridView else Icons.AutoMirrored.Outlined.List,
                            contentDescription = "Toggle layout"
                        )
                    }
                    IconButton(onClick = onNavigateToTrash) {
                        Icon(
                            Icons.Outlined.Delete,
                            contentDescription = "Recycle bin",
                            tint = MaterialTheme.colorScheme.error
                        )
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
                        ProfileAvatar(currentUser)
                    }
                }
            )
        }
    ) { paddingValues ->
        if (currentUser == null) {
            EmptyFavView(
                title = "Sign in to see your bowl",
                message = "Your reviews, statuses, and notes will appear here after login."
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                ProfileHeader(
                    user = currentUser,
                    total = myLogs.size,
                    watching = myLogs.count { it.status == "Watching" },
                    completed = myLogs.count { it.status == "Completed" },
                    plan = myLogs.count { it.status == "Plan to Watch" }
                )

                if (myLogs.isNotEmpty()) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = BowlSpacing.md, vertical = BowlSpacing.sm),
                        horizontalArrangement = Arrangement.spacedBy(BowlSpacing.xs)
                    ) {
                        items(filterOptions) { filter ->
                            FilterChip(
                                selected = selectedFilter == filter,
                                onClick = { selectedFilter = filter },
                                label = { Text(filter) },
                                colors = bowlChipColors(),
                                border = null
                            )
                        }
                    }
                }

                when {
                    myLogs.isEmpty() -> EmptyFavView(
                        title = "No review history",
                        message = "Tap search in the bottom bar to start logging anime."
                    )
                    filteredLogs.isEmpty() -> EmptyFavView(
                        title = "Nothing in this filter",
                        message = "Choose another status or update an existing review."
                    )
                    isGridMode -> LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            bottom = 112.dp,
                            start = BowlSpacing.sm,
                            end = BowlSpacing.sm,
                            top = BowlSpacing.xs
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
                    else -> LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            bottom = 112.dp,
                            start = BowlSpacing.md,
                            end = BowlSpacing.md,
                            top = BowlSpacing.xs
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
                }
            }
        }
    }
}

@Composable
private fun ProfileHeader(
    user: FirebaseUser?,
    total: Int,
    watching: Int,
    completed: Int,
    plan: Int
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BowlSpacing.md, vertical = BowlSpacing.xs),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(BowlRadius.xl),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(Modifier.padding(BowlSpacing.lg)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(BowlSpacing.md)
            ) {
                ProfileAvatar(user, size = 58)
                Column(Modifier.weight(1f)) {
                    Text(
                        text = user?.displayName ?: "Anime Bowl User",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = user?.email ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Spacer(Modifier.height(BowlSpacing.lg))
            Row(horizontalArrangement = Arrangement.spacedBy(BowlSpacing.sm)) {
                StatPill("Total", total, Modifier.weight(1f))
                StatPill("Watching", watching, Modifier.weight(1f))
            }
            Spacer(Modifier.height(BowlSpacing.sm))
            Row(horizontalArrangement = Arrangement.spacedBy(BowlSpacing.sm)) {
                StatPill("Completed", completed, Modifier.weight(1f))
                StatPill("Plan", plan, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun StatPill(label: String, value: Int, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(BowlRadius.md),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
    ) {
        Column(Modifier.padding(BowlSpacing.md)) {
            Text(
                value.toString(),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ProfileAvatar(user: FirebaseUser?, size: Int = 30) {
    if (user?.photoUrl != null) {
        AsyncImage(
            model = user.photoUrl,
            contentDescription = "Profile",
            modifier = Modifier
                .size(size.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    } else {
        Icon(
            Icons.Filled.AccountCircle,
            contentDescription = "Login",
            modifier = Modifier.size(size.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
