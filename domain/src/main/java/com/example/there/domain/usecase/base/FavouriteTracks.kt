package com.example.there.domain.usecase.base

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.repo.IFavouriteTrackRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

abstract class InsertTrackUseCase<Track>(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: IFavouriteTrackRepository<Track>
) : CompletableUseCaseWithInput<Track>(schedulersProvider) {
    override fun createCompletable(input: Track): Completable = repository.insertTrack(input)
}

abstract class IsTrackSavedUseCase<Track>(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: IFavouriteTrackRepository<Track>
) : SingleUseCaseWithInput<Track, Boolean>(schedulersProvider) {
    override fun createSingle(input: Track): Single<Boolean> = repository.isTrackSaved(input)
}

abstract class DeleteTrackUseCase<Track>(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: IFavouriteTrackRepository<Track>
) : CompletableUseCaseWithInput<Track>(schedulersProvider) {
    override fun createCompletable(input: Track): Completable = repository.deleteTrack(input)
}

abstract class GetFavouriteTracksUseCase<Track>(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: IFavouriteTrackRepository<Track>
) : FlowableUseCase<List<Track>>(schedulersProvider) {
    override val flowable: Flowable<List<Track>>
        get() = repository.favouriteTracks
}