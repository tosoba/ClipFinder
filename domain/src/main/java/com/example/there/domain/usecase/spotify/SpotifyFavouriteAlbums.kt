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
import javax.inject.Inject


class GetFavouriteAlbums @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyLocalRepo
) : FlowableUseCase<List<AlbumEntity>>(schedulersProvider) {

    override val flowable: Flowable<List<AlbumEntity>>
        get() = repository.favouriteAlbums
}

class InsertAlbum @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyLocalRepo
) : CompletableUseCaseWithArgs<AlbumEntity>(schedulersProvider) {

    override fun createCompletable(args: AlbumEntity): Completable = repository.insertAlbum(args)
}

class IsAlbumSaved @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyLocalRepo
) : SingleUseCaseWithArgs<AlbumEntity, Boolean>(schedulersProvider) {

    override fun createSingle(args: AlbumEntity): Single<Boolean> = repository.isAlbumSaved(args)
}

class DeleteAlbum @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyLocalRepo
) : CompletableUseCaseWithArgs<AlbumEntity>(schedulersProvider) {

    override fun createCompletable(args: AlbumEntity): Completable = repository.deleteAlbum(args)
}