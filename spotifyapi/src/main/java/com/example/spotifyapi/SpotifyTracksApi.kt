package com.example.spotifyapi

import com.example.core.retrofit.NetworkResponse
import com.example.spotifyapi.model.SpotifyErrorResponse
import com.example.spotifyapi.models.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotifyTracksApi {
    @GET("tracks")
    fun getTracks(
            @Header("Authorization") authorization: String,
            @Query("ids") ids: String
    ): Single<NetworkResponse<TracksResponse, SpotifyErrorResponse>>

    @GET("tracks/{id}")
    fun getTrack(
            @Header("Authorization") authorization: String,
            @Path("id") id: String
    ): Single<NetworkResponse<Track, SpotifyErrorResponse>>

    @GET("audio-features/{id}")
    fun getAudioFeaturesForTrack(
            @Header("Authorization") authorization: String,
            @Path("id") trackId: String
    ): Single<NetworkResponse<AudioFeatures, SpotifyErrorResponse>>

    @GET("audio-features")
    fun getAudioFeaturesForTracks(
            @Header("Authorization") authorization: String,
            @Query("ids") ids: String
    ): Single<NetworkResponse<AudioFeaturesResponse, SpotifyErrorResponse>>

    @GET("audio-analysis/{id}")
    fun getAudioAnalysisForTrack(
            @Header("Authorization") authorization: String,
            @Path("id") trackId: String
    ): Single<NetworkResponse<AudioAnalysis, SpotifyErrorResponse>>
}