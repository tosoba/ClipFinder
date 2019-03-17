package com.example.api

import com.example.api.model.DiscoverResponse
import com.vpaliy.soundcloud.model.TrackEntity
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface SoundCloudApi {
    @GET("selections")
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
}