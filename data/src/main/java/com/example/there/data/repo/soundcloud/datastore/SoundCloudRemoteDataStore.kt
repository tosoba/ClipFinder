package com.example.there.data.repo.soundcloud.datastore

import com.example.there.data.api.soundcloud.SoundCloudApi
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
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
}