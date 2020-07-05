package com.example.there.domain.usecase.soundcloud

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.soundcloud.SoundCloudDiscoverEntity
import com.example.there.domain.repo.soundcloud.ISoundCloudRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single

class DiscoverSoundCloud(
    schedulersProvider: UseCaseSchedulersProvider,
    private val remote: ISoundCloudRemoteDataStore
) : SingleUseCase<SoundCloudDiscoverEntity>(schedulersProvider) {
    override val result: Single<SoundCloudDiscoverEntity> get() = remote.discover
} 