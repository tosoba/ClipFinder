package com.example.there.domain.usecase.soundcloud

import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import com.example.there.domain.repo.soundcloud.ISoundCloudRepository
import com.example.there.domain.usecase.base.SingleUseCaseWithInput
import io.reactivex.Scheduler
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class GetTracksFromPlaylist @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: ISoundCloudRepository
) : SingleUseCaseWithInput<String, List<SoundCloudTrackEntity>>(subscribeOnScheduler, observeOnScheduler) {
    override fun createSingle(input: String): Single<List<SoundCloudTrackEntity>> = repository.getTracksFromPlaylist(input)
}