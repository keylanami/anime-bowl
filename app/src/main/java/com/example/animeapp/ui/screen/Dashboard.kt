
package com.example.animeapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.animeapp.ui.components.AnimeList
import com.example.animeapp.ui.components.ErrorView
import com.example.animeapp.ui.components.LoadingView
import com.example.animeapp.ui.state.AnimeUiState
import com.example.animeapp.ui.viewmodel.AnimeViewModel

@Composable
fun AnimeScreen(viewModel: AnimeViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val showSearch by remember { mutableStateOf(false) }
    var q by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadTopAnime()
    }




    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        OutlinedTextField(
            value = q,
            onValueChange = { q = it },
            label = { Text("Search anime") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { viewModel.searchAnime(q) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))


        when (uiState) {
            is AnimeUiState.Loading -> {
                LoadingView()
            }

            is AnimeUiState.Success -> {
                val animeList = (uiState as AnimeUiState.Success).animeList
                AnimeList(animeList)
            }

            is AnimeUiState.Error -> {
                val message = (uiState as AnimeUiState.Error).message
                ErrorView(message)
            }
        }
    }
}