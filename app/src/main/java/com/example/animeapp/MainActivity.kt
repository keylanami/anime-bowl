package com.example.animeapp

import AnimeScreen
import AnimeViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.example.animeapp.data.remote.RetrofitClient
import com.example.animeapp.data.repository.AnimeRepositoryImpl
//import com.example.animeapp.ui.screen.AnimeScreen
//import com.example.animeapp.ui.viewmodel.AnimeViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val api = RetrofitClient.api

        val repository = AnimeRepositoryImpl(api)

        val viewModel = AnimeViewModel(repository)

        setContent {
            MaterialTheme {
                AnimeScreen(viewModel = viewModel)
            }
        }
    }
}
