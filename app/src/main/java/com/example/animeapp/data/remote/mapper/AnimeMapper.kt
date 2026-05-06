package com.example.animeapp.data.remote.mapper

import com.example.animeapp.data.local.entity.FavAnimeEntity
import com.example.animeapp.data.model.Anime
import com.example.animeapp.data.remote.dto.AnimeDTO

fun AnimeDTO.toAnime(): Anime{
    return Anime(
        id = 0,
        mal_id = mal_id,
        title = title,
        type = type?: "unknown",
        episodes = episodes?: 0,
        rank = rank?: 0,
        score = score?: 0.0,
        image_url = images.jpg.image_url,
        status = "Plan to watch",
        userNote = ""
    )
}

