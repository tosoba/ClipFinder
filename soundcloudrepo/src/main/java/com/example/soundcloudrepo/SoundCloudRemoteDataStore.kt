package com.example.soundcloudrepo

import com.clipfinder.soundcloud.api.SoundCloudApi
import com.clipfinder.soundcloud.api.SoundCloudApiV2
import com.clipfinder.soundcloud.api.model.SoundCloudPlaylistApiModel
import com.clipfinder.soundcloud.api.model.SoundCloudSystemPlaylistApiModel
import com.clipfinder.soundcloud.api.model.SoundCloudTrack
import com.example.soundcloudrepo.mapper.domain
import com.example.there.domain.entity.soundcloud.SoundCloudDiscoverEntity
import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import com.example.there.domain.repo.soundcloud.ISoundCloudRemoteDataStore
import com.vpaliy.soundcloud.SoundCloudService
import io.reactivex.Single

class SoundCloudRemoteDataStore(
    private val apiV2: SoundCloudApiV2,
    private val api: SoundCloudApi,
    private val service: SoundCloudService
) : ISoundCloudRemoteDataStore {

    override val mixedSelections: Single<SoundCloudDiscoverEntity>
        get() = Single.just(SoundCloudDiscoverEntity(emptyList(), emptyList()))

    override fun getTracksFromPlaylist(
        id: String
    ): Single<List<SoundCloudTrackEntity>> = api.getTracksFromPlaylist(id)
        .map { it.map(SoundCloudTrack::domain) }

    override fun getTracks(
        ids: List<String>
    ): Single<List<SoundCloudTrackEntity>> = apiV2.getTracks(ids.joinToString(separator = ","))
        .map { it.map(SoundCloudTrack::domain) }

    override fun getSimilarTracks(
        id: String
    ): Single<List<SoundCloudTrackEntity>> = apiV2.getRelatedTracks(id)
        .map {
            it.collection?.run {
                map(SoundCloudTrack::domain)
            } ?: run {
                emptyList<SoundCloudTrackEntity>()
            }
        }
}
