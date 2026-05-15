package com.example.animeapp.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.animeapp.data.model.Anime
import com.example.animeapp.ui.viewmodel.AnimeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    viewModel: AnimeViewModel,
    animeId: Int,
    onNavigateUp: () -> Unit
) {
    val context = LocalContext.current
    val isEditMode = animeId > 0
    val selectedAnime by viewModel.selectedAnime.collectAsState()


    var title by remember { mutableStateOf("") }
    var score by remember { mutableStateOf("0.0") }
    var userNote by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Plan to Watch") }

    val statusOptions = listOf("Plan to Watch", "Watching", "Completed")


    LaunchedEffect(animeId) {
        if (isEditMode) {
            viewModel.getAnimeById(animeId)
        } else {
            viewModel.clearSelectedAnime()
        }
    }

    LaunchedEffect(selectedAnime) {
        selectedAnime?.let {
            title = it.title
            score = it.score.toString()
            userNote = it.userNote
            status = it.status
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Edit Diary" else "Log Anime Baru") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) { Icon(Icons.Filled.ArrowBack, contentDescription = "Kembali") }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Judul Anime") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = score,
                onValueChange = { score = it },
                label = { Text("Rating (0.0 - 10.0)") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Status Tontonan:")
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
                label = { Text("Review / Catatan (Opsional)") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                maxLines = 5
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {

                    if (title.isBlank()) {
                        Toast.makeText(context, "Judul tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    val parsedScore = score.toDoubleOrNull()
                    if (parsedScore == null || parsedScore < 0.0 || parsedScore > 10.0) {
                        Toast.makeText(context, "Format rating salah (0-10)!", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val animeToSave = Anime(
                        id = if (isEditMode) animeId else 0,
                        mal_id = selectedAnime?.mal_id,
                        title = title,
                        type = selectedAnime?.type ?: "Custom",
                        episodes = selectedAnime?.episodes ?: 0,
                        score = parsedScore,
                        rank = selectedAnime?.rank ?: 0,
                        image_url = selectedAnime?.image_url ?: "",
                        status = status,
                        userNote = userNote
                    )

                    if (isEditMode) viewModel.updateAnime(animeToSave)
                    else viewModel.insertAnime(animeToSave)

                    Toast.makeText(context, "Diary berhasil disimpan!", Toast.LENGTH_SHORT).show()
                    onNavigateUp()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEditMode) "Simpan Perubahan" else "Simpan ke Diary")
            }
        }
    }
}