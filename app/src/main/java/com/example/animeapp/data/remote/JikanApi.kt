package com.example.animeapp.data.remote

import com.example.animeapp.data.remote.dto.TopAnimeResponseDTO
import retrofit2.http.GET
import retrofit2.http.Query


interface JikanApi {


    @GET("top/anime")
    suspend fun getTopAnime(): TopAnimeResponseDTO

    @GET("anime")
    suspend fun searchAnime(@Query("q") query: String): TopAnimeResponseDTO

}