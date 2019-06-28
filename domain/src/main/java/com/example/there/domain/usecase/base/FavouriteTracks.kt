package com.example.there.domain.usecase.base

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.repo.IFavouriteTrackRepo
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

abstract class InsertTrackUseCase<Track>(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repo: IFavouriteTrackRepo<Track>
) : CompletableUseCaseWithArgs<Track>(schedulersProvider) {
    override fun createCompletable(args: Track): Completable = repo.insertTrack(args)
}

abstract class IsTrackSavedUseCase<Track>(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repo: IFavouriteTrackRepo<Track>
) : SingleUseCaseWithArgs<Track, Boolean>(schedulersProvider) {
    override fun createSingle(args: Track): Single<Boolean> = repo.isTrackSaved(args)
}

abstract class DeleteTrackUseCase<Track>(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repo: IFavouriteTrackRepo<Track>
) : CompletableUseCaseWithArgs<Track>(schedulersProvider) {
    override fun createCompletable(args: Track): Completable = repo.deleteTrack(args)
}

abstract class GetFavouriteTracksUseCase<Track>(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repo: IFavouriteTrackRepo<Track>
) : FlowableUseCase<List<Track>>(schedulersProvider) {
    override val flowable: Flowable<List<Track>>
        get() = repo.favouriteTracks
}