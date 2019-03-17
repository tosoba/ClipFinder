package com.example.there.domain.usecase.spotify

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.base.CompletableUseCaseWithInput
import com.example.there.domain.usecase.base.FlowableUseCase
import com.example.there.domain.usecase.base.SingleUseCaseWithInput
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class GetFavouriteSpotifyPlaylists @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyRepository
) : FlowableUseCase<List<PlaylistEntity>>(schedulersProvider) {

    override val flowable: Flowable<List<PlaylistEntity>>
        get() = repository.favouritePlaylists
}

class InsertSpotifyPlaylist @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyRepository
) : CompletableUseCaseWithInput<PlaylistEntity>(schedulersProvider) {

    override fun createCompletable(input: PlaylistEntity): Completable = repository.insertPlaylist(input)
}

class IsSpotifyPlaylistSaved @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyRepository
) : SingleUseCaseWithInput<PlaylistEntity, Boolean>(schedulersProvider) {

    override fun createSingle(input: PlaylistEntity): Single<Boolean> = repository.isPlaylistSaved(input)
}

class DeleteSpotifyPlaylist @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyRepository
) : CompletableUseCaseWithInput<PlaylistEntity>(schedulersProvider) {

    override fun createCompletable(input: PlaylistEntity): Completable = repository.deletePlaylist(input)
}