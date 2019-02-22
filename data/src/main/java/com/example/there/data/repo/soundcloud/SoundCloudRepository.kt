package com.example.there.data.repo.soundcloud

import com.example.there.domain.entity.soundcloud.SoundCloudDiscoverEntity
import com.example.there.domain.repo.soundcloud.ISoundCloudRepository
import com.example.there.domain.repo.soundcloud.datastore.ISoundCloudDbDataStore
import com.example.there.domain.repo.soundcloud.datastore.ISoundCloudRemoteDataStore
import io.reactivex.Single
import javax.inject.Inject

class SoundCloudRepository @Inject constructor(
        private val remote: ISoundCloudRemoteDataStore,
        private val db: ISoundCloudDbDataStore
) : ISoundCloudRepository {

    override val discover: Single<SoundCloudDiscoverEntity>
        get() = remote.discover
}