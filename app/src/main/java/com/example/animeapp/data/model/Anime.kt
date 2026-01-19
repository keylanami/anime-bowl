package com.example.animeapp.data.model

data class Anime(
    val id: Int,
    val title: String,
    val type: String,
    val episodes: Int,
    val score: Double,
    val rank: Int,
    val image_url: String
)