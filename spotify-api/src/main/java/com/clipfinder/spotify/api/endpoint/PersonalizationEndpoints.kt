package com.clipfinder.spotify.api.endpoint

import com.clipfinder.core.model.NetworkResponse
import com.clipfinder.spotify.api.model.ArtistsPagingObject
import com.clipfinder.spotify.api.model.ErrorResponse
import com.clipfinder.spotify.api.model.TracksPagingObject
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PersonalizationEndpoints {
    @GET("me/top/tracks")
    fun getUsersTopTracks(
        @Header("Authorization") authorization: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null,
        @Query("time_range") timeRange: String? = null
    ): Single<NetworkResponse<TracksPagingObject, ErrorResponse>>

    @GET("me/top/artists")
    fun getUsersTopArtists(
        @Header("Authorization") authorization: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null,
        @Query("time_range") timeRange: String? = null
    ): Single<NetworkResponse<ArtistsPagingObject, ErrorResponse>>
}
