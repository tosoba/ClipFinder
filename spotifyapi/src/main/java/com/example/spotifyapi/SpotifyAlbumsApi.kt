package com.example.spotifyapi

import com.example.core.SpotifyDefaults
import com.example.core.retrofit.NetworkResponse
import com.example.spotifyapi.models.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotifyAlbumsApi {
    @GET("albums/{id}")
    fun getAlbum(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): Single<NetworkResponse<Album, ErrorResponse>>

    @GET("albums/{id}/tracks")
    fun getAlbumsTracks(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): Single<NetworkResponse<PagingObject<SimpleTrack>, ErrorResponse>>

    @GET("albums")
    fun getAlbums(
        @Header("Authorization") authorization: String,
        @Query("ids") ids: String,
        @Query("market") market: String = SpotifyDefaults.COUNTRY
    ): Single<NetworkResponse<AlbumsResponse, ErrorResponse>>
}
