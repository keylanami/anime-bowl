package com.example.animeapp.data.repository

import com.example.animeapp.data.model.Anime

interface AnimeRepository {
    suspend fun getTopAnime(): List<Anime>
    suspend fun searchAnime(query: String): List<Anime>
}