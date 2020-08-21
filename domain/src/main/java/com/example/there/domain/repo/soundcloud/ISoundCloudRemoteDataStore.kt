package com.example.there.domain.repo.soundcloud

import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import io.reactivex.Single

interface ISoundCloudRemoteDataStore {
    fun getTracksFromPlaylist(id: String): Single<List<SoundCloudTrackEntity>>
    fun getTracks(ids: List<String>): Single<List<SoundCloudTrackEntity>>
    fun getSimilarTracks(id: String): Single<List<SoundCloudTrackEntity>>
}
