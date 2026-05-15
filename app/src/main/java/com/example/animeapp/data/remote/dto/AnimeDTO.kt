package com.example.animeapp.data.remote.dto

data class AnimeDTO(
    val mal_id: Int,
    val title: String,
    val type: String?,
    val episodes: Int?,
    val score: Double?,
    val rank: Int?,
    val images: ImagesDTO,
    val is_in_trash: Boolean = false
)
