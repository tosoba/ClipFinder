package com.clipfinder.soundcloud.api

import com.clipfinder.soundcloud.api.model.DiscoverResponse
import com.clipfinder.soundcloud.api.model.SoundCloudRelatedTracksResponse
import com.clipfinder.soundcloud.api.model.SoundCloudTrack
import com.clipfinder.soundcloud.api.model.TrackEntity
import com.clipfinder.soundcloud.api.model.front.SoundCloudFrontResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SoundCloudApiV2 {
    @GET("front")
    fun front(
        @Query("client_id") clientId: String = SoundCloudAuth.key,
        @Query("app_locale") locale: String = "us"
    ): Single<SoundCloudFrontResponse>

    @GET("mixed-selections")
    fun mixedSelections(
        @Query("client_id") clientId: String = SoundCloudAuth.key,
        @Query("app_locale") locale: String = "us"
    ): Single<DiscoverResponse>

    @GET("tracks")
    fun getTracks(
        @Query("ids") ids: String,
        @Query("client_id") clientId: String = SoundCloudAuth.key,
        @Query("app_locale") locale: String = "us"
    ): Single<List<SoundCloudTrack>>

    @GET("tracks/{id}/related")
    fun getRelatedTracks(
        @Path("id") id: String,
        @Query("client_id") clientId: String = SoundCloudAuth.key
    ): Single<SoundCloudRelatedTracksResponse>
}
