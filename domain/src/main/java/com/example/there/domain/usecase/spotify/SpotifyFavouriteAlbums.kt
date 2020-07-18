package com.example.there.domain.usecase.spotify

import com.example.core.ext.RxSchedulers
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.repo.spotify.ISpotifyLocalRepo
import com.example.there.domain.usecase.base.CompletableUseCaseWithArgs
import com.example.there.domain.usecase.base.FlowableUseCase
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class GetFavouriteAlbums(
    schedulers: RxSchedulers,
    private val repository: ISpotifyLocalRepo
) : FlowableUseCase<List<AlbumEntity>>(schedulers) {
    override val result: Flowable<List<AlbumEntity>> get() = repository.favouriteAlbums
}

class InsertAlbum(
    schedulers: RxSchedulers,
    private val repository: ISpotifyLocalRepo
) : CompletableUseCaseWithArgs<AlbumEntity>(schedulers) {
    override fun run(args: AlbumEntity): Completable = repository.insertAlbum(args)
}

class IsAlbumSaved(
    schedulers: RxSchedulers,
    private val repository: ISpotifyLocalRepo
) : SingleUseCaseWithArgs<String, Boolean>(schedulers) {
    override fun run(args: String): Single<Boolean> = repository.isAlbumSaved(args)
}

class DeleteAlbum(
    schedulers: RxSchedulers,
    private val repository: ISpotifyLocalRepo
) : CompletableUseCaseWithArgs<AlbumEntity>(schedulers) {
    override fun run(args: AlbumEntity): Completable = repository.deleteAlbum(args)
}
