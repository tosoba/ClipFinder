package com.example.api

import com.example.api.model.DiscoverResponse
import com.example.api.model.SoundCloudRelatedTracksResponse
import com.vpaliy.soundcloud.model.TrackEntity
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SoundCloudApiV2 {
    @GET("mixed-selections")
    fun discover(
        @Query("client_id") clientId: String = SoundCloudAuth.key,
        @Query("app_locale") locale: String = "us"
    ): Single<DiscoverResponse>

    @GET("tracks")
    fun getTracks(
        @Query("ids") ids: String,
        @Query("client_id") clientId: String = SoundCloudAuth.key,
        @Query("app_locale") locale: String = "us"
    ): Single<List<TrackEntity>>

    @GET("tracks/{id}/related")
    fun getRelatedTracks(
        @Path("id") id: String,
        @Query("client_id") clientId: String = SoundCloudAuth.key
    ): Single<SoundCloudRelatedTracksResponse>
}
