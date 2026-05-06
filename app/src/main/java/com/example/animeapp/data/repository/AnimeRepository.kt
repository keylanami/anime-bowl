package com.example.animeapp.data.repository

import com.example.animeapp.data.model.Anime
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {
    suspend fun getTopAnime(): List<Anime>
    suspend fun searchAnime(query: String): List<Anime>


    fun getFavAnime(): Flow<List<Anime>>

    suspend fun getAnimeById(id: Int): Anime?
    suspend fun insertAnime(anime: Anime)
    suspend fun updateAnime(anime: Anime)
    suspend fun deleteAnime(anime: Anime)


    suspend fun deleteByMalId(malId: Int)
    suspend fun isFavorite(malId: Int): Boolean

}