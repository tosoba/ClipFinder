package com.example.api

import com.example.api.model.SoundCloudTrackApiModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SoundCloudApi {
    @GET("playlists/{id}/tracks")
    fun getTracksFromPlaylist(
            @Path("id") id: String,
            @Query("client_id") clientId: String = SoundCloudAuth.key
    ): Single<List<SoundCloudTrackApiModel>>
}