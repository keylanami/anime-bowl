package com.example.animeapp.data.remote.mapper

import com.example.animeapp.data.local.entity.FavAnimeEntity
import com.example.animeapp.data.model.Anime

fun Anime.toFavAnimeEntity(): FavAnimeEntity {
    return FavAnimeEntity(
        id = id,
        mal_id = mal_id,
        title = title,
        type = type,
        episodes = episodes,
        score = score,
        rank = rank,
        image_url = image_url,
        status = status,
        user_note = userNote
    )
}

fun FavAnimeEntity.toAnime(): Anime {
    return Anime (
        id = id,
        mal_id = mal_id,
        title = title,
        type = type,
        episodes = episodes,
        score = score,
        rank = rank,
        image_url = image_url,
        status = status,
        userNote = user_note
    )
}