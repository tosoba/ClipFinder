package com.example.there.domain.repo.soundcloud

import com.example.there.domain.entity.soundcloud.SoundCloudDiscoverEntity
import io.reactivex.Single

interface ISoundCloudRepository {
    val discover: Single<SoundCloudDiscoverEntity>
}