package com.example.there.data.api.soundcloud

import com.example.there.data.api.SoundCloudClient
import com.example.there.data.response.DiscoverResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface SoundCloudApi {
    @GET("selections")
    fun discover(
            @Query("client_id") clientId: String = SoundCloudClient.key,
            @Query("app_locale") locale: String = "us"
    ): Single<DiscoverResponse>
}