package com.example.there.domain.usecase.soundcloud

import com.example.core.ext.RxSchedulers
import com.example.there.domain.entity.soundcloud.SoundCloudDiscoverEntity
import com.example.there.domain.repo.soundcloud.ISoundCloudRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single

class DiscoverSoundCloud(
    schedulers: RxSchedulers,
    private val remote: ISoundCloudRemoteDataStore
) : SingleUseCase<SoundCloudDiscoverEntity>(schedulers) {
    override val result: Single<SoundCloudDiscoverEntity> get() = remote.mixedSelections
} 