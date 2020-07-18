package com.example.there.domain.usecase.spotify

import com.example.core.ext.RxSchedulers
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.repo.spotify.ISpotifyLocalRepo
import com.example.there.domain.usecase.base.CompletableUseCaseWithArgs
import com.example.there.domain.usecase.base.FlowableUseCase
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class GetFavouriteArtists(
    schedulers: RxSchedulers,
    private val repository: ISpotifyLocalRepo
) : FlowableUseCase<List<ArtistEntity>>(schedulers) {
    override val result: Flowable<List<ArtistEntity>> get() = repository.favouriteArtists
}

class InsertArtist(
    schedulers: RxSchedulers,
    private val repository: ISpotifyLocalRepo
) : CompletableUseCaseWithArgs<ArtistEntity>(schedulers) {
    override fun run(args: ArtistEntity): Completable = repository.insertArtist(args)
}

class IsArtistSaved(
    schedulers: RxSchedulers,
    private val repository: ISpotifyLocalRepo
) : SingleUseCaseWithArgs<String, Boolean>(schedulers) {
    override fun run(args: String): Single<Boolean> = repository.isArtistSaved(args)
}

class DeleteArtist(
    schedulers: RxSchedulers,
    private val repository: ISpotifyLocalRepo
) : CompletableUseCaseWithArgs<ArtistEntity>(schedulers) {
    override fun run(args: ArtistEntity): Completable = repository.deleteArtist(args)
}
