package com.example.animeapp.data.local.db

import androidx.room.Database
import com.example.animeapp.data.local.dao.FavAnimeDao
import com.example.animeapp.data.local.entity.FavAnimeEntity
import androidx.room.RoomDatabase

@Database(
    entities = [FavAnimeEntity::class],
    version = 2,
    exportSchema = false
)

abstract class AnimeDatabase: RoomDatabase() {
    abstract fun favAnimeDao(): FavAnimeDao
}