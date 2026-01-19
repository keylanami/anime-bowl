package com.example.animeapp.data.repository

import com.example.animeapp.data.model.Anime
import com.example.animeapp.data.remote.JikanApi
import com.example.animeapp.data.remote.mapper.toAnime

class AnimeRepositoryImpl(private val api: JikanApi): AnimeRepository {

    override suspend fun getTopAnime(): List<Anime> {
        val response = api.getTopAnime()
        return response.data.map { dTO -> dTO.toAnime() }
    }

    override suspend fun searchAnime(query: String): List<Anime> {
        val response = api.searchAnime(query)
        return response.data.map { dTO -> dTO.toAnime() }
    }
}