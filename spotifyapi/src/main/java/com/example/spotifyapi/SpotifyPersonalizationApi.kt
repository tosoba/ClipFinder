package com.example.spotifyapi

import com.example.core.SpotifyDefaults
import com.example.core.retrofit.NetworkResponse
import com.example.spotifyapi.model.SpotifyErrorResponse
import com.example.spotifyapi.models.*
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
    ): Single<NetworkResponse<TracksResult, SpotifyErrorResponse>>

    @GET("me/top/artists")
    fun getCurrentUsersTopArtists(
        @Header("Authorization") authorization: String,
        @Query("limit") limit: Int = SpotifyDefaults.LIMIT,
        @Query("offset") offset: Int = SpotifyDefaults.OFFSET
    ): Single<NetworkResponse<ArtistsResult, SpotifyErrorResponse>>

    @GET("me/playlists")
    fun getCurrentUsersPlaylists(
        @Header("Authorization") authorization: String,
        @Query("limit") limit: Int = SpotifyDefaults.LIMIT,
        @Query("offset") offset: Int = SpotifyDefaults.OFFSET
    ): Single<NetworkResponse<PlaylistsResult, SpotifyErrorResponse>>

    @GET("me/tracks")
    fun getCurrentUsersSavedTracks(
        @Header("Authorization") authorization: String,
        @Query("limit") limit: Int = SpotifyDefaults.LIMIT,
        @Query("offset") offset: Int = SpotifyDefaults.OFFSET
    ): Single<NetworkResponse<SavedTracksResult, SpotifyErrorResponse>>

    @GET("me/albums")
    fun getCurrentUsersSavedAlbums(
        @Header("Authorization") authorization: String,
        @Query("limit") limit: Int = SpotifyDefaults.LIMIT,
        @Query("offset") offset: Int = SpotifyDefaults.OFFSET
    ): Single<NetworkResponse<SavedAlbumsResult, SpotifyErrorResponse>>
}
