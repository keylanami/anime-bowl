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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.example.animeapp.R
import com.example.animeapp.data.remote.FirestoreHelper
import com.example.animeapp.data.remote.FirebaseAuthHelper
import com.example.animeapp.data.remote.encodeImageUriToBase64
import com.example.animeapp.data.remote.getImageModel
import com.example.animeapp.ui.viewmodel.AnimeViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

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
            Toast.makeText(context, "Log updated!", Toast.LENGTH_SHORT).show()
            onNavigateUp()
        }
        isUploading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Review Log") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        cropperLauncher.launch(
                            CropImageContractOptions(null, CropImageOptions(
                                imageSourceIncludeGallery = true,
                                imageSourceIncludeCamera = true,
                                fixAspectRatio = true
                            ))
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = if (imageUri != null) imageUri else getImageModel(selectedAnime?.image_url),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Text(
                    text = "Tap to change image",
                    color = Color.White,
                    modifier = Modifier.background(Color.Black.copy(alpha = 0.5f)).padding(8.dp)
                )
            }

            Text(text = selectedAnime?.title ?: "Loading...", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

            OutlinedTextField(
                value = score, onValueChange = { score = it }, label = { Text("Rating (0.0 - 10.0)") },
                modifier = Modifier.fillMaxWidth(), singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary, unfocusedBorderColor = Color.Gray)
            )

            Text("Watch Status", fontWeight = FontWeight.SemiBold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                statusOptions.forEach { opt ->
                    FilterChip(selected = status == opt, onClick = { status = opt }, label = { Text(opt) })
                }
            }

            OutlinedTextField(
                value = userNote, onValueChange = { userNote = it }, label = { Text("Notes / Review") },
                modifier = Modifier.fillMaxWidth().height(120.dp)
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
                            val user = FirebaseAuthHelper.signInWithGoogle(context, context.getString(R.string.default_web_client_id))
                            if (user != null) {
                                currentUser = user
                                performSave(user.uid, parsedScore)
                            } else {
                                isUploading = false
                                Toast.makeText(context, "Login diperlukan untuk menyimpan log", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            performSave(currentUser!!.uid, parsedScore)
                        }
                    }
                },
                enabled = !isUploading,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                if (isUploading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                else Text("Save Log")
            }
        }
    }
}