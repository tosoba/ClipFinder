package com.clipfinder.core.soundcloud.repo

import com.clipfinder.core.soundcloud.model.ISoundCloudPlaylistSelection
import com.clipfinder.core.soundcloud.model.ISoundCloudTrack
import io.reactivex.Single

interface ISoundCloudRepo {
    fun mixedSelections(clientId: String): Single<List<ISoundCloudPlaylistSelection>>
    fun featuredTracks(
        kind: String,
        genre: String,
        clientId: String
    ): Single<List<ISoundCloudTrack>>
    fun getTracksFromPlaylist(id: String, clientId: String): Single<List<ISoundCloudTrack>>
    fun getTracks(ids: List<String>, clientId: String): Single<List<ISoundCloudTrack>>
    fun getSimilarTracks(id: String, clientId: String): Single<List<ISoundCloudTrack>>
}
