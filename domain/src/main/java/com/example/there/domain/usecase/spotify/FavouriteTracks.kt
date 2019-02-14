package com.example.there.domain.usecase.spotify

import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.base.CompletableUseCaseWithInput
import com.example.there.domain.usecase.base.FlowableUseCase
import com.example.there.domain.usecase.base.SingleUseCaseWithInput
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class GetFavouriteTracks @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: ISpotifyRepository
) : FlowableUseCase<List<TrackEntity>>(subscribeOnScheduler, observeOnScheduler) {

    override val flowable: Flowable<List<TrackEntity>>
        get() = repository.favouriteTracks
}

class InsertTrack @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: ISpotifyRepository
) : CompletableUseCaseWithInput<TrackEntity>(subscribeOnScheduler, observeOnScheduler) {

    override fun createCompletable(input: TrackEntity): Completable = repository.insertTrack(input)
}

class IsTrackSaved @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: ISpotifyRepository
) : SingleUseCaseWithInput<TrackEntity, Boolean>(subscribeOnScheduler, observeOnScheduler) {

    override fun createSingle(input: TrackEntity): Single<Boolean> = repository.isTrackSaved(input)
}

class DeleteTrack @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: ISpotifyRepository
) : CompletableUseCaseWithInput<TrackEntity>(subscribeOnScheduler, observeOnScheduler) {

    override fun createCompletable(input: TrackEntity): Completable = repository.deleteTrack(input)
}