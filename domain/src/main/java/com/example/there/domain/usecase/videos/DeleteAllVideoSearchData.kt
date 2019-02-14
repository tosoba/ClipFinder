package com.example.there.domain.usecase.videos

import com.example.there.domain.repo.videos.IVideosRepository
import com.example.there.domain.usecase.base.CompletableUseCase
import io.reactivex.Completable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

class DeleteAllVideoSearchData @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: IVideosRepository
) : CompletableUseCase(subscribeOnScheduler, observeOnScheduler) {

    override val completable: Completable
        get() = repository.deleteAllVideoSearchData()
}