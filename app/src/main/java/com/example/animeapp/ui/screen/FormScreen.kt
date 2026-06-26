package com.example.animeapp.ui.screen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.example.animeapp.R
import com.example.animeapp.data.remote.FirebaseAuthHelper
import com.example.animeapp.data.remote.FirestoreHelper
import com.example.animeapp.data.remote.encodeImageUriToBase64
import com.example.animeapp.data.remote.getImageModel
import com.example.animeapp.ui.theme.BowlRadius
import com.example.animeapp.ui.theme.BowlSpacing
import com.example.animeapp.ui.theme.bowlChipColors
import com.example.animeapp.ui.theme.bowlTextFieldColors
import com.example.animeapp.ui.viewmodel.AnimeViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    viewModel: AnimeViewModel,
    animeId: Int,
    onNavigateUp: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val selectedAnime by viewModel.selectedAnime.collectAsState()

    var currentUser by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }
    DisposableEffect(Unit) {
        val listener = FirebaseAuth.AuthStateListener { auth -> currentUser = auth.currentUser }
        FirebaseAuth.getInstance().addAuthStateListener(listener)
        onDispose { FirebaseAuth.getInstance().removeAuthStateListener(listener) }
    }

    var score by remember { mutableStateOf("") }
    var ratingSlider by remember { mutableFloatStateOf(0f) }
    var userNote by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Plan to Watch") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }

    val statusOptions = listOf("Plan to Watch", "Watching", "Completed")

    val cropperLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) imageUri = result.uriContent
    }

    LaunchedEffect(animeId) {
        if (animeId > 0) viewModel.getAnimeById(animeId)
    }

    LaunchedEffect(selectedAnime) {
        selectedAnime?.let {
            score = it.score.toString()
            ratingSlider = it.score.toFloat().coerceIn(0f, 10f)
            userNote = it.userNote
            status = it.status.ifBlank { "Plan to Watch" }
        }
    }

    suspend fun performSave(uid: String, parsedScore: Double) {
        var finalImageUrl = selectedAnime?.image_url ?: ""
        if (imageUri != null) {
            val base64Image = encodeImageUriToBase64(context, imageUri!!)
            if (base64Image != null) finalImageUrl = base64Image
        }

        val animeToSave = selectedAnime?.copy(
            id = if (animeId > 0) animeId else 0,
            score = parsedScore,
            status = status,
            userNote = userNote,
            userId = uid,
            image_url = finalImageUrl
        )

        animeToSave?.let {
            if (animeId > 0) viewModel.updateAnime(it) else viewModel.insertAnime(it)
            FirestoreHelper.saveReviewToServer(uid, it)
            Toast.makeText(context, "Log updated", Toast.LENGTH_SHORT).show()
            onNavigateUp()
        }
        isUploading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Review Log", fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = BowlSpacing.md)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(BowlSpacing.md)
        ) {
            ImagePickerCard(
                image = if (imageUri != null) imageUri else getImageModel(selectedAnime?.image_url),
                onClick = {
                    cropperLauncher.launch(
                        CropImageContractOptions(
                            null,
                            CropImageOptions(
                                imageSourceIncludeGallery = true,
                                imageSourceIncludeCamera = true,
                                fixAspectRatio = true
                            )
                        )
                    )
                }
            )

            Column {
                Text(
                    text = selectedAnime?.title ?: "Loading anime",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = selectedAnime?.type?.ifBlank { "Anime" } ?: "Anime",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Surface(
                shape = RoundedCornerShape(BowlRadius.lg),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(BowlSpacing.md),
                    verticalArrangement = Arrangement.spacedBy(BowlSpacing.md)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Rating", style = MaterialTheme.typography.titleSmall)
                        Text(
                            text = score.ifBlank { "0.0" },
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Slider(
                        value = ratingSlider,
                        onValueChange = {
                            ratingSlider = it
                            score = ((it * 10).roundToInt() / 10.0).toString()
                        },
                        valueRange = 0f..10f,
                        steps = 19
                    )
                    OutlinedTextField(
                        value = score,
                        onValueChange = {
                            score = it
                            it.toFloatOrNull()?.let { parsed ->
                                ratingSlider = parsed.coerceIn(0f, 10f)
                            }
                        },
                        label = { Text("Rating (0.0 - 10.0)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(BowlRadius.md),
                        colors = bowlTextFieldColors()
                    )
                }
            }

            Text("Watch Status", style = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(BowlSpacing.xs)) {
                statusOptions.forEach { opt ->
                    FilterChip(
                        selected = status == opt,
                        onClick = { status = opt },
                        label = { Text(opt) },
                        colors = bowlChipColors(),
                        border = null
                    )
                }
            }

            OutlinedTextField(
                value = userNote,
                onValueChange = { userNote = it },
                label = { Text("Notes / Review") },
                placeholder = { Text("What stood out?") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                shape = RoundedCornerShape(BowlRadius.lg),
                colors = bowlTextFieldColors()
            )

            Button(
                onClick = {
                    val parsedScore = score.toDoubleOrNull()
                    if (parsedScore == null || parsedScore !in 0.0..10.0) {
                        Toast.makeText(context, "Please enter valid rating (0-10)", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    isUploading = true

                    scope.launch {
                        if (currentUser == null) {
                            val user = FirebaseAuthHelper.signInWithGoogle(
                                context,
                                context.getString(R.string.default_web_client_id)
                            )
                            if (user != null) {
                                currentUser = user
                                performSave(user.uid, parsedScore)
                            } else {
                                isUploading = false
                                Toast.makeText(context, "Login is required to save a log", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            performSave(currentUser!!.uid, parsedScore)
                        }
                    }
                },
                enabled = !isUploading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                if (isUploading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                } else {
                    Text("Save Log")
                }
            }

            Spacer(Modifier.height(BowlSpacing.lg))
        }
    }
}

@Composable
private fun ImagePickerCard(
    image: Any?,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(248.dp)
            .clip(RoundedCornerShape(BowlRadius.xl))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(BowlSpacing.md)
                .clip(RoundedCornerShape(BowlRadius.pill))
                .background(Color.Black.copy(alpha = 0.48f))
                .padding(horizontal = BowlSpacing.md, vertical = BowlSpacing.xs),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(BowlSpacing.xs)
        ) {
            Icon(
                Icons.Outlined.Image,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "Change image",
                color = Color.White,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}
