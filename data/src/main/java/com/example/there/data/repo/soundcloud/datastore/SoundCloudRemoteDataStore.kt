package com.example.there.data.repo.soundcloud.datastore

import com.example.there.data.api.soundcloud.SoundCloudApi
import com.example.there.data.entity.soundcloud.SoundCloudPlaylist
import com.example.there.data.entity.soundcloud.SoundCloudSystemPlaylist
import com.example.there.data.mapper.soundcloud.domain
import com.example.there.domain.entity.soundcloud.SoundCloudDiscoverEntity
import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import com.example.there.domain.repo.soundcloud.datastore.ISoundCloudRemoteDataStore
import com.vpaliy.soundcloud.SoundCloudService
import com.vpaliy.soundcloud.model.TrackEntity
import io.reactivex.Single
import javax.inject.Inject

class SoundCloudRemoteDataStore @Inject constructor(
        private val api: SoundCloudApi,
        private val service: SoundCloudService
) : ISoundCloudRemoteDataStore {

    override val discover: Single<SoundCloudDiscoverEntity>
        get() = api.discover().map { response ->
            SoundCloudDiscoverEntity(
                    playlists = response.collection.map {
                        it.playlists?.map(SoundCloudPlaylist::domain) ?: emptyList()
                    }.flatten(),
                    systemPlaylists = response.collection.map {
                        it.systemPlaylists?.map(SoundCloudSystemPlaylist::domain) ?: emptyList()
                    }.flatten()
            )
        }

    override fun getTracksFromPlaylist(
            id: String
    ): Single<List<SoundCloudTrackEntity>> = service.fetchPlaylistTracks(id)
            .map { it.map(TrackEntity::domain) }

    override fun getTracks(
            ids: List<String>
    ): Single<List<SoundCloudTrackEntity>> = api.getTracks(ids.joinToString(separator = ","))
            .map { it.map(TrackEntity::domain) }
}