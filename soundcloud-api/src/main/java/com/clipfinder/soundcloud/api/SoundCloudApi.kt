package com.clipfinder.soundcloud.api

import com.clipfinder.soundcloud.api.model.SoundCloudTrack
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SoundCloudApi {
    @GET("playlists/{id}/tracks")
    fun getTracksFromPlaylist(
        @Path("id") id: String,
        @Query("client_id") clientId: String = SoundCloudAuth.key
    ): Single<List<SoundCloudTrack>>
}
