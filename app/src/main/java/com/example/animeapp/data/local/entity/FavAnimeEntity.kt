package com.example.animeapp.data.local.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_anime")
data class FavAnimeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val type: String,
    val episodes: Int,
    val score: Double,
    val rank: Int,
    val image_url: String,

    val status: String,
    val user_note: String
)
