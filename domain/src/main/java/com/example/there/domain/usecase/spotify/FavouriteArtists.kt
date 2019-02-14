package com.example.there.domain.usecase.spotify

import com.example.there.domain.entity.spotify.ArtistEntity
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

class GetFavouriteArtists @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: ISpotifyRepository
) : FlowableUseCase<List<ArtistEntity>>(subscribeOnScheduler, observeOnScheduler) {

    override val flowable: Flowable<List<ArtistEntity>>
        get() = repository.favouriteArtists
}

class InsertArtist @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: ISpotifyRepository
) : CompletableUseCaseWithInput<ArtistEntity>(subscribeOnScheduler, observeOnScheduler) {

    override fun createCompletable(input: ArtistEntity): Completable = repository.insertArtist(input)
}

class IsArtistSaved @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: ISpotifyRepository
) : SingleUseCaseWithInput<ArtistEntity, Boolean>(subscribeOnScheduler, observeOnScheduler) {

    override fun createSingle(input: ArtistEntity): Single<Boolean> = repository.isArtistSaved(input)
}

class DeleteArtist @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: ISpotifyRepository
) : CompletableUseCaseWithInput<ArtistEntity>(subscribeOnScheduler, observeOnScheduler) {

    override fun createCompletable(input: ArtistEntity): Completable = repository.deleteArtist(input)
}