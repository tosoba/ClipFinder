package com.example.there.domain.repo.soundcloud

import com.example.there.domain.entity.soundcloud.SoundCloudDiscoverEntity
import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import io.reactivex.Single

interface ISoundCloudRepository {
    val discover: Single<SoundCloudDiscoverEntity>
    fun getTracksFromPlaylist(id: String): Single<List<SoundCloudTrackEntity>>
}