package com.example.spotifyapi

import com.example.core.SpotifyDefaults
import com.example.core.retrofit.NetworkResponse
import com.example.spotifyapi.model.SpotifyErrorResponse
import com.example.spotifyapi.models.SearchAllResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifySearchApi {
    @GET("search")
    fun search(
        @Header("Authorization") authorization: String,
        @Query("q") query: String,
        @Query("type") type: String,
        @Query("offset") offset: Int = SpotifyDefaults.OFFSET,
        @Query("limit") limit: Int = SpotifyDefaults.LIMIT
    ): Single<NetworkResponse<SearchAllResponse, SpotifyErrorResponse>>
}
