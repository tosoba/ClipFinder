package com.example.soundcloudrepo

import com.example.api.SoundCloudApi
import com.example.api.SoundCloudApiV2
import com.example.api.model.SoundCloudPlaylistApiModel
import com.example.api.model.SoundCloudSystemPlaylistApiModel
import com.example.api.model.SoundCloudTrackApiModel
import com.example.soundcloudrepo.mapper.domain
import com.example.there.domain.entity.soundcloud.SoundCloudDiscoverEntity
import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import com.example.there.domain.repo.soundcloud.ISoundCloudRemoteDataStore
import com.vpaliy.soundcloud.SoundCloudService
import com.vpaliy.soundcloud.model.TrackEntity
import io.reactivex.Single

class SoundCloudRemoteDataStore(
    private val apiV2: SoundCloudApiV2,
    private val api: SoundCloudApi,
    private val service: SoundCloudService
) : ISoundCloudRemoteDataStore {

    override val discover: Single<SoundCloudDiscoverEntity>
        get() = apiV2.discover().map { response ->
            SoundCloudDiscoverEntity(
                playlists = response.collection.map {
                    it.playlists?.map(SoundCloudPlaylistApiModel::domain) ?: emptyList()
                }.flatten(),
                systemPlaylists = response.collection.map {
                    it.systemPlaylists?.map(SoundCloudSystemPlaylistApiModel::domain)
                        ?: emptyList()
                }.flatten()
            )
        }

    override fun getTracksFromPlaylist(
        id: String
    ): Single<List<SoundCloudTrackEntity>> = api.getTracksFromPlaylist(id)
        .map { it.map(SoundCloudTrackApiModel::domain) }

    override fun getTracks(
        ids: List<String>
    ): Single<List<SoundCloudTrackEntity>> = apiV2.getTracks(ids.joinToString(separator = ","))
        .map { it.map(TrackEntity::domain) }

    override fun getSimilarTracks(
        id: String
    ): Single<List<SoundCloudTrackEntity>> = apiV2.getRelatedTracks(id)
        .map {
            it.collection?.run {
                map(SoundCloudTrackApiModel::domain)
            } ?: run {
                emptyList<SoundCloudTrackEntity>()
            }
        }
}
