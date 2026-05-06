package com.example.animeapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.animeapp.data.local.entity.FavAnimeEntity

@Dao

interface FavAnimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFav(anime: FavAnimeEntity)

    @Update
    suspend fun updateAnime(anime: FavAnimeEntity)

    @Delete
    suspend fun deleteAnime(anime: FavAnimeEntity)


    @Query("delete from fav_anime where mal_id = :malId")
    suspend fun deleteFavAnime(malId: Int)

    @Query("select * from fav_anime")
    suspend fun getFavAnime(): List<FavAnimeEntity>

    @Query("select exists(select 1 from fav_anime where id = :id)")
    suspend fun isFavorite(id: Int): Boolean

}

