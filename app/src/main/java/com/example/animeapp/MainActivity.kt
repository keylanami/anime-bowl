package com.example.animeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.room.Room
import com.example.animeapp.data.local.dao.FavAnimeDao
import com.example.animeapp.data.local.db.AnimeDatabase
import com.example.animeapp.data.remote.RetrofitClient
import com.example.animeapp.data.repository.AnimeRepositoryImpl
import com.example.animeapp.ui.navigation.AppNavigation
import com.example.animeapp.ui.viewmodel.AnimeViewModel
import com.example.animeapp.ui.screen.AnimeScreen
//import com.example.animeapp.ui.viewmodel.AnimeViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Room.databaseBuilder(
            applicationContext,
            AnimeDatabase::class.java,
            "anime_db"
        ).build()

        val dao = database.favAnimeDao()

        val api = RetrofitClient.api

        val repository = AnimeRepositoryImpl(api, dao)

        val viewModel = AnimeViewModel(repository)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(viewModel = viewModel)
                }

            }
        }
    }
}
