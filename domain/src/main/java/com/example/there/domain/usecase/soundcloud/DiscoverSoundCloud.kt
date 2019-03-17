package com.example.there.domain.usecase.soundcloud

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.soundcloud.SoundCloudDiscoverEntity
import com.example.there.domain.repo.soundcloud.ISoundCloudRepository
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class DiscoverSoundCloud @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISoundCloudRepository
) : SingleUseCase<SoundCloudDiscoverEntity>(schedulersProvider) {

    override val single: Single<SoundCloudDiscoverEntity>
        get() = repository.discover
} 