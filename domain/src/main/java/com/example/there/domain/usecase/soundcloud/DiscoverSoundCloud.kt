package com.example.there.domain.usecase.soundcloud

import com.example.there.domain.entity.soundcloud.SoundCloudDiscoverEntity
import com.example.there.domain.repo.soundcloud.ISoundCloudRepository
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Scheduler
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class DiscoverSoundCloud @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: ISoundCloudRepository
) : SingleUseCase<SoundCloudDiscoverEntity>(subscribeOnScheduler, observeOnScheduler) {

    override val single: Single<SoundCloudDiscoverEntity>
        get() = repository.discover
} 