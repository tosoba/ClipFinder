package com.example.spotifyapi

import com.example.core.SpotifyDefaults
import com.example.core.retrofit.NetworkResponse
import com.example.spotifyapi.model.SpotifyErrorResponse
import com.example.spotifyapi.models.ArtistsPagedResponse
import com.example.spotifyapi.models.TracksPagedResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifyPersonalizationApi {
    @GET("me/top/tracks")
    fun getCurrentUsersTopTracks(
            @Header("Authorization") authorization: String,
            @Query("limit") limit: Int = SpotifyDefaults.LIMIT,
            @Query("offset") offset: Int = SpotifyDefaults.OFFSET
    ): Single<NetworkResponse<TracksPagedResponse, SpotifyErrorResponse>>

    @GET("me/top/artists")
    fun getCurrentUsersTopArtists(
            @Header("Authorization") authorization: String,
            @Query("limit") limit: Int = SpotifyDefaults.LIMIT,
            @Query("offset") offset: Int = SpotifyDefaults.OFFSET
    ): Single<NetworkResponse<ArtistsPagedResponse, SpotifyErrorResponse>>
}