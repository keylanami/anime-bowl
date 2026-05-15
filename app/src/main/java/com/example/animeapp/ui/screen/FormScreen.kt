package com.example.animeapp.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.animeapp.ui.viewmodel.AnimeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    viewModel: AnimeViewModel,
    animeId: Int,
    onNavigateUp: () -> Unit
) {
    val context = LocalContext.current
    val selectedAnime by viewModel.selectedAnime.collectAsState()

    var score by remember { mutableStateOf("") }
    var userNote by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Plan to Watch") }

    val statusOptions = listOf("Plan to Watch", "Watching", "Completed")

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Review Log"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
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
            AsyncImage(
                model = selectedAnime?.image_url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Text(
                text = selectedAnime?.title ?: "Loading...",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = score,
                onValueChange = { score = it },
                label = { Text("Rating (0.0 - 10.0)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Gray
                )
            )

            Text("Watch Status", fontWeight = FontWeight.SemiBold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                statusOptions.forEach { opt ->
                    FilterChip(
                        selected = status == opt,
                        onClick = { status = opt },
                        label = { Text(opt) }
                    )
                }
            }

            OutlinedTextField(
                value = userNote,
                onValueChange = { userNote = it },
                label = { Text("Notes / Review") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            Button(
                onClick = {
                    val parsedScore = score.toDoubleOrNull()
                    if (parsedScore == null || parsedScore !in 0.0..10.0) {
                        Toast.makeText(
                            context,
                            "Please enter valid rating (0-10)",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }
                    if (status !in statusOptions) {
                        Toast.makeText(context, "Invalid status selected", Toast.LENGTH_SHORT)
                            .show()
                        return@Button
                    }

                    val animeToSave = selectedAnime?.copy(
                        id = if (animeId > 0) animeId else 0,
                        score = parsedScore,
                        status = status,
                        userNote = userNote
                    )

                    animeToSave?.let {
                        if (animeId > 0) viewModel.updateAnime(it)
                        else viewModel.insertAnime(it)
                        Toast.makeText(context, "Log updated!", Toast.LENGTH_SHORT).show()
                        onNavigateUp()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Save Log")
            }
        }
    }
}