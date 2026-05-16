package com.example.animeapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.animeapp.data.local.entity.FavAnimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavAnimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnime(anime: FavAnimeEntity)

    @Update
    suspend fun updateAnime(anime: FavAnimeEntity)

    @Delete
    suspend fun deleteAnime(anime: FavAnimeEntity)

    @Query("select * from fav_anime where is_in_trash = 0 order by id desc")
    fun getFavAnimeSorted(): Flow<List<FavAnimeEntity>>

    @Query("select * from fav_anime where is_in_trash = 1 order by id desc")
    fun getTrashedAnime(): Flow<List<FavAnimeEntity>>

    @Query("select * from fav_anime where id = :id")
    suspend fun getAnimeById(id: Int): FavAnimeEntity?

    @Query("select exists(select 1 from fav_anime where mal_id = :malId)")
    suspend fun isFavorite(malId: Int): Boolean

    @Query("delete from fav_anime where mal_id = :malId")
    suspend fun deleteByMalId(malId: Int)

    @Query("delete from fav_anime where id = :id")
    suspend fun moveToTrash(id: Int)

    @Query("update fav_anime set is_in_trash = 0 where id = :id")
    suspend fun restoreFromTrash(id: Int)
    
}