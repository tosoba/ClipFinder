package com.example.there.domain.usecase.base

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.repo.IFavouritePlaylistRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

abstract class InsertPlaylistUseCase<Playlist>(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: IFavouritePlaylistRepository<Playlist>
) : CompletableUseCaseWithInput<Playlist>(schedulersProvider) {
    override fun createCompletable(input: Playlist): Completable = repository.insertPlaylist(input)
}

abstract class IsPlaylistSavedUseCase<Playlist>(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: IFavouritePlaylistRepository<Playlist>
) : SingleUseCaseWithInput<Playlist, Boolean>(schedulersProvider) {
    override fun createSingle(input: Playlist): Single<Boolean> = repository.isPlaylistSaved(input)
}

abstract class DeletePlaylistUseCase<Playlist>(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: IFavouritePlaylistRepository<Playlist>
) : CompletableUseCaseWithInput<Playlist>(schedulersProvider) {
    override fun createCompletable(input: Playlist): Completable = repository.deletePlaylist(input)
}

abstract class GetFavouritePlaylistsUseCase<Playlist>(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: IFavouritePlaylistRepository<Playlist>
) : FlowableUseCase<List<Playlist>>(schedulersProvider) {
    override val flowable: Flowable<List<Playlist>>
        get() = repository.favouritePlaylists
}