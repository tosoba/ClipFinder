package com.example.there.domain.repo.soundcloud.datastore

import com.example.there.domain.entity.soundcloud.SoundCloudDiscoverEntity
import io.reactivex.Single

interface ISoundCloudRemoteDataStore {
    val discover: Single<SoundCloudDiscoverEntity>
}