package com.example.there.domain.usecase.base

import com.example.core.ext.RxSchedulers
import com.example.there.domain.repo.IFavouriteTrackRepo
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

abstract class InsertTrackUseCase<Track>(
    schedulers: RxSchedulers,
    private val repo: IFavouriteTrackRepo<Track>
) : CompletableUseCaseWithArgs<Track>(schedulers) {
    override fun run(args: Track): Completable = repo.insertTrack(args)
}

abstract class IsTrackSavedUseCase<Track>(
    schedulers: RxSchedulers,
    private val repo: IFavouriteTrackRepo<Track>
) : SingleUseCaseWithArgs<String, Boolean>(schedulers) {
    override fun run(args: String): Single<Boolean> = repo.isTrackSaved(args)
}

abstract class DeleteTrackUseCase<Track>(
    schedulers: RxSchedulers,
    private val repo: IFavouriteTrackRepo<Track>
) : CompletableUseCaseWithArgs<Track>(schedulers) {
    override fun run(args: Track): Completable = repo.deleteTrack(args)
}

abstract class GetFavouriteTracksUseCase<Track>(
    schedulers: RxSchedulers,
    private val repo: IFavouriteTrackRepo<Track>
) : FlowableUseCase<List<Track>>(schedulers) {
    override val result: Flowable<List<Track>> get() = repo.favouriteTracks
}
