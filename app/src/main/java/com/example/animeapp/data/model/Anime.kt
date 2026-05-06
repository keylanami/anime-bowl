package com.example.animeapp.data.model

data class Anime(
    val id: Int = 0,
    val mal_id: Int? = null,
    val title: String,
    val type: String,
    val episodes: Int,
    val score: Double,
    val rank: Int,
    val image_url: String,

    val status: String = "Plan to watch",
    val userNote: String = ""
)