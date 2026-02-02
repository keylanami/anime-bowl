package com.example.animeapp.data.remote.mapper

import com.example.animeapp.data.local.entity.FavAnimeEntity
import com.example.animeapp.data.model.Anime

fun Anime.toFavAnimeEntity(): FavAnimeEntity {
    return FavAnimeEntity(
        id = id,
        title = title,
        type = type,
        episodes = episodes,
        score = score,
        rank = rank,
        image_url = image_url
    )
}

fun FavAnimeEntity.toAnime(): Anime {
    return Anime (
        id = id,
        title = title,
        type = type,
        episodes = episodes,
        score = score,
        rank = rank,
        image_url = image_url
    )
}