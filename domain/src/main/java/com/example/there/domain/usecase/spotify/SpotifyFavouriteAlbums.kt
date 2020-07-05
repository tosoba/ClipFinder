package com.example.there.domain.usecase.spotify

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.repo.spotify.ISpotifyLocalRepo
import com.example.there.domain.usecase.base.CompletableUseCaseWithArgs
import com.example.there.domain.usecase.base.FlowableUseCase
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class GetFavouriteAlbums(
    schedulersProvider: UseCaseSchedulersProvider,
    private val repository: ISpotifyLocalRepo
) : FlowableUseCase<List<AlbumEntity>>(schedulersProvider) {
    override val result: Flowable<List<AlbumEntity>> get() = repository.favouriteAlbums
}

class InsertAlbum(
    schedulersProvider: UseCaseSchedulersProvider,
    private val repository: ISpotifyLocalRepo
) : CompletableUseCaseWithArgs<AlbumEntity>(schedulersProvider) {
    override fun run(args: AlbumEntity): Completable = repository.insertAlbum(args)
}

class IsAlbumSaved(
    schedulersProvider: UseCaseSchedulersProvider,
    private val repository: ISpotifyLocalRepo
) : SingleUseCaseWithArgs<String, Boolean>(schedulersProvider) {
    override fun run(args: String): Single<Boolean> = repository.isAlbumSaved(args)
}

class DeleteAlbum(
    schedulersProvider: UseCaseSchedulersProvider,
    private val repository: ISpotifyLocalRepo
) : CompletableUseCaseWithArgs<AlbumEntity>(schedulersProvider) {
    override fun run(args: AlbumEntity): Completable = repository.deleteAlbum(args)
}
