package com.example.animeapp.data.repository

import com.example.animeapp.data.local.dao.FavAnimeDao
import com.example.animeapp.data.model.Anime
import com.example.animeapp.data.remote.JikanApi
import com.example.animeapp.data.remote.mapper.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AnimeRepositoryImpl(
    private val api: JikanApi,
    private val dao: FavAnimeDao
): AnimeRepository {

    override suspend fun getTopAnime(): List<Anime> {
        return api.getTopAnime().data.map { it.toAnime() }
    }

    override suspend fun searchAnime(query: String): List<Anime> {
        return api.searchAnime(query).data.map { it.toAnime() }
    }

    override fun getFavAnime(): Flow<List<Anime>> {
        return dao.getFavAnimeSorted().map { list ->
            list.map { it.toAnime() }
        }
    }

    override suspend fun getAnimeById(id: Int): Anime? {
        return dao.getAnimeById(id)?.toAnime()
    }

    override suspend fun insertAnime(anime: Anime) {
        dao.insertAnime(anime.toFavAnimeEntity())
    }

    override suspend fun updateAnime(anime: Anime) {
        dao.updateAnime(anime.toFavAnimeEntity())
    }

    override suspend fun deleteAnime(anime: Anime) {
        dao.deleteAnime(anime.toFavAnimeEntity())
    }

    override suspend fun isFavorite(malId: Int): Boolean {
        return dao.isFavorite(malId)
    }

    override suspend fun deleteByMalId(malId: Int) {
        dao.deleteByMalId(malId)
    }
}