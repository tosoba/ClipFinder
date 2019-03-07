package com.example.there.data.repo.soundcloud.datastore

import com.example.there.data.api.soundcloud.SoundCloudApi
import com.example.there.data.entity.soundcloud.SoundCloudPlaylist
import com.example.there.data.entity.soundcloud.SoundCloudSystemPlaylist
import com.example.there.data.mapper.soundcloud.domain
import com.example.there.domain.entity.soundcloud.SoundCloudDiscoverEntity
import com.example.there.domain.repo.soundcloud.datastore.ISoundCloudRemoteDataStore
import com.vpaliy.soundcloud.SoundCloudService
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
                        it.playlists.map(SoundCloudPlaylist::domain)
                    }.flatten(),
                    systemPlaylists = response.collection.map {
                        it.systemPlaylists.map(SoundCloudSystemPlaylist::domain)
                    }.flatten()
            )
        }
}