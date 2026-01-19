package com.example.animeapp.data.local.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_anime")
data class FavAnimeEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val type: String,
    val episodes: Int,
    val image_url: String
)
