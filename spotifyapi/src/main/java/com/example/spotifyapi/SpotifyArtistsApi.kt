package com.example.spotifyapi

import com.example.core.SpotifyDefaults
import com.example.core.retrofit.NetworkResponse
import com.example.spotifyapi.models.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotifyArtistsApi {
    @GET("artists/{id}")
    fun getArtist(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): Single<NetworkResponse<Artist, ErrorResponse>>

    @GET("artists/{id}/albums")
    fun getArtistsAlbums(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @Query("offset") offset: Int
    ): Single<NetworkResponse<PagingObject<SimpleAlbum>, ErrorResponse>>

    @GET("artists/{id}/top-tracks")
    fun getArtistsTopTracks(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @Query("country") country: String = SpotifyDefaults.COUNTRY
    ): Single<NetworkResponse<TracksResponse, ErrorResponse>>

    @GET("artists/{id}/related-artists")
    fun getArtistsRelatedArtists(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): Single<NetworkResponse<ArtistsResponse, ErrorResponse>>

    @GET("artists")
    fun getArtists(
        @Header("Authorization") authorization: String,
        @Query("ids") ids: String
    ): Single<NetworkResponse<ArtistsResponse, ErrorResponse>>
}