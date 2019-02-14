package com.example.there.domain.usecase.spotify

import com.example.there.domain.entity.spotify.AlbumEntity
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


class GetFavouriteAlbums @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: ISpotifyRepository
) : FlowableUseCase<List<AlbumEntity>>(subscribeOnScheduler, observeOnScheduler) {

    override val flowable: Flowable<List<AlbumEntity>>
        get() = repository.favouriteAlbums
}

class InsertAlbum @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: ISpotifyRepository
) : CompletableUseCaseWithInput<AlbumEntity>(subscribeOnScheduler, observeOnScheduler) {

    override fun createCompletable(input: AlbumEntity): Completable = repository.insertAlbum(input)
}

class IsAlbumSaved @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: ISpotifyRepository
) : SingleUseCaseWithInput<AlbumEntity, Boolean>(subscribeOnScheduler, observeOnScheduler) {

    override fun createSingle(input: AlbumEntity): Single<Boolean> = repository.isAlbumSaved(input)
}

class DeleteAlbum @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: ISpotifyRepository
) : CompletableUseCaseWithInput<AlbumEntity>(subscribeOnScheduler, observeOnScheduler) {

    override fun createCompletable(input: AlbumEntity): Completable = repository.deleteAlbum(input)
}