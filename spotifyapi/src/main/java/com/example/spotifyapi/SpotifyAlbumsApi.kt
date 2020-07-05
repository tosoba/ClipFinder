package com.example.spotifyapi

import com.example.core.SpotifyDefaults
import com.example.core.retrofit.NetworkResponse
import com.example.spotifyapi.models.*
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotifyAlbumsApi {
    @GET("albums/{id}")
    fun getAlbum(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): NetworkResponse<Album, ErrorResponse>

    @GET("albums/{id}/tracks")
    fun getAlbumsTracks(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): NetworkResponse<PagingObject<SimpleTrack>, ErrorResponse>

    @GET("albums")
    fun getAlbums(
        @Header("Authorization") authorization: String,
        @Query("ids") ids: String,
        @Query("market") market: String = SpotifyDefaults.COUNTRY
    ): NetworkResponse<AlbumsResponse, ErrorResponse>
}
