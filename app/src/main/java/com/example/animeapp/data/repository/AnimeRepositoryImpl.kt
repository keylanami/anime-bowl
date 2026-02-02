package com.example.animeapp.data.repository

import com.example.animeapp.data.local.dao.FavAnimeDao
import com.example.animeapp.data.model.Anime
import com.example.animeapp.data.remote.JikanApi
import com.example.animeapp.data.remote.mapper.toAnime
import com.example.animeapp.data.remote.mapper.toFavAnimeEntity

class AnimeRepositoryImpl(
    private val api: JikanApi,
    private val dao: FavAnimeDao
): AnimeRepository {

    override suspend fun getTopAnime(): List<Anime> {
        val response = api.getTopAnime()
        return response.data.map { dTO -> dTO.toAnime() }
    }

    override suspend fun searchAnime(query: String): List<Anime> {
        val response = api.searchAnime(query)
        return response.data.map { dTO -> dTO.toAnime() }
    }

    override suspend fun getFavAnime(): List<Anime> {
        val response = dao.getFavAnime()
        return response.map { it.toAnime() }
    }

    override suspend fun addToFav(anime: Anime) {
        dao.addToFav(anime.toFavAnimeEntity())
    }

    override suspend fun deleteFav(id: Int) {
        dao.deleteFavAnime(id)
    }

    override suspend fun isFavorite(id: Int): Boolean {
        val response = dao.isFavorite(id)
        return response
    }
}