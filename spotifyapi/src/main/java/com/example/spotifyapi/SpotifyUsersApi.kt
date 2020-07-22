package com.example.spotifyapi

import com.example.core.retrofit.NetworkResponse
import com.example.spotifyapi.models.ErrorResponse
import com.example.spotifyapi.models.PlaylistTracksResponse
import com.example.spotifyapi.models.SpotifyUserInformation
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotifyUsersApi {
    @GET("me")
    fun getCurrentUser(
        @Header("Authorization") authorization: String
    ): Single<NetworkResponse<SpotifyUserInformation, ErrorResponse>>

    @GET("users/{user_id}")
    fun getUser(
        @Header("Authorization") authorization: String,
        @Path("user_id") userId: String
    ): Single<NetworkResponse<SpotifyUserInformation, ErrorResponse>>

    @GET("users/{user_id}/playlists/{playlist_id}/tracks")
    fun getPlaylistTracks(
        @Header("Authorization") authorization: String,
        @Path("user_id") userId: String,
        @Path("playlist_id") playlistId: String,
        @Query("offset") offset: Int
    ): Single<NetworkResponse<PlaylistTracksResponse, ErrorResponse>>
}
