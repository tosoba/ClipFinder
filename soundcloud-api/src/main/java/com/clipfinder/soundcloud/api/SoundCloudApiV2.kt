package com.clipfinder.soundcloud.api

import com.clipfinder.soundcloud.api.model.SoundCloudRelatedTracksResponse
import com.clipfinder.soundcloud.api.model.SoundCloudTrack
import com.clipfinder.soundcloud.api.model.collection.SoundCollectionResponse
import com.clipfinder.soundcloud.api.model.mixed.selections.SoundCloudMixedSelectionsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SoundCloudApiV2 {
    @GET("featured_tracks/{kind}/{genre}")
    fun featuredTracks(
        @Path("kind") kind: String = "top",
        @Path("genre") genre: String = "all-music",
        @Query("client_id") clientId: String,
        @Query("app_locale") locale: String = "us"
    ): Single<SoundCollectionResponse>

    @GET("mixed-selections")
    fun mixedSelections(
        @Query("client_id") clientId: String,
        @Query("app_locale") locale: String = "us"
    ): Single<SoundCloudMixedSelectionsResponse>

    @GET("tracks")
    fun getTracks(
        @Query("ids") ids: String,
        @Query("client_id") clientId: String,
        @Query("app_locale") locale: String = "us"
    ): Single<List<SoundCloudTrack>>

    @GET("tracks/{id}/related")
    fun getRelatedTracks(
        @Path("id") id: String,
        @Query("client_id") clientId: String
    ): Single<SoundCloudRelatedTracksResponse>
}
