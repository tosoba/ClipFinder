package com.example.there.domain.usecase.base

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.repo.IFavouritePlaylistRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

abstract class InsertPlaylistUseCase<Playlist>(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: IFavouritePlaylistRepository<Playlist>
) : CompletableUseCaseWithArgs<Playlist>(schedulersProvider) {
    override fun createCompletable(args: Playlist): Completable = repository.insertPlaylist(args)
}

abstract class IsPlaylistSavedUseCase<Playlist>(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: IFavouritePlaylistRepository<Playlist>
) : SingleUseCaseWithArgs<Playlist, Boolean>(schedulersProvider) {
    override fun createSingle(args: Playlist): Single<Boolean> = repository.isPlaylistSaved(args)
}

abstract class DeletePlaylistUseCase<Playlist>(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: IFavouritePlaylistRepository<Playlist>
) : CompletableUseCaseWithArgs<Playlist>(schedulersProvider) {
    override fun createCompletable(args: Playlist): Completable = repository.deletePlaylist(args)
}

abstract class GetFavouritePlaylistsUseCase<Playlist>(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: IFavouritePlaylistRepository<Playlist>
) : FlowableUseCase<List<Playlist>>(schedulersProvider) {
    override val flowable: Flowable<List<Playlist>>
        get() = repository.favouritePlaylists
}