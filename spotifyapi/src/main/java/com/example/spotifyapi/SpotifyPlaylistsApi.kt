package com.example.spotifyapi

import com.example.core.retrofit.NetworkResponse
import com.example.spotifyapi.models.ErrorResponse
import com.example.spotifyapi.models.PlaylistTracksResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotifyPlaylistsApi {
    @GET("playlists/{playlist_id}/tracks")
    fun getPlaylistTracks(
        @Header("Authorization") authorization: String,
        @Path("playlist_id") playlistId: String,
        @Query("offset") offset: Int
    ): Single<NetworkResponse<PlaylistTracksResponse, ErrorResponse>>
}