package com.example.animeapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.animeapp.data.local.entity.FavAnimeEntity

@Dao

interface FavAnimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFav(anime: FavAnimeEntity)

    @Query("delete from fav_anime where id = :id")
    suspend fun deleteFavAnime(id: Int)

    @Query("select * from fav_anime")
    suspend fun getFavAnime(): List<FavAnimeEntity>

    @Query("select exists(select 1 from fav_anime where id = :id)")
    suspend fun isFavorite(id: Int): Boolean

}

