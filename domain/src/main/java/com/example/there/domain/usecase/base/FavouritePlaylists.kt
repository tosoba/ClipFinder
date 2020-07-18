package com.example.there.domain.usecase.base

import com.example.core.ext.RxSchedulers
import com.example.there.domain.repo.IFavouritePlaylistRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

abstract class InsertPlaylistUseCase<Playlist>(
    schedulers: RxSchedulers,
    private val repository: IFavouritePlaylistRepository<Playlist>
) : CompletableUseCaseWithArgs<Playlist>(schedulers) {
    override fun run(args: Playlist): Completable = repository.insertPlaylist(args)
}

abstract class IsPlaylistSavedUseCase<Playlist>(
    schedulers: RxSchedulers,
    private val repository: IFavouritePlaylistRepository<Playlist>
) : SingleUseCaseWithArgs<String, Boolean>(schedulers) {
    override fun run(args: String): Single<Boolean> = repository.isPlaylistSaved(args)
}

abstract class DeletePlaylistUseCase<Playlist>(
    schedulers: RxSchedulers,
    private val repository: IFavouritePlaylistRepository<Playlist>
) : CompletableUseCaseWithArgs<Playlist>(schedulers) {
    override fun run(args: Playlist): Completable = repository.deletePlaylist(args)
}

abstract class GetFavouritePlaylistsUseCase<Playlist>(
    schedulers: RxSchedulers,
    private val repository: IFavouritePlaylistRepository<Playlist>
) : FlowableUseCase<List<Playlist>>(schedulers) {
    override val result: Flowable<List<Playlist>> get() = repository.favouritePlaylists
}