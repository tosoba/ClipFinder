package com.example.there.domain.usecase.spotify

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.repo.spotify.ISpotifyLocalRepo
import com.example.there.domain.usecase.base.CompletableUseCaseWithArgs
import com.example.there.domain.usecase.base.FlowableUseCase
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class GetFavouriteArtists(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyLocalRepo
) : FlowableUseCase<List<ArtistEntity>>(schedulersProvider) {

    override val result: Flowable<List<ArtistEntity>>
        get() = repository.favouriteArtists
}

class InsertArtist(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyLocalRepo
) : CompletableUseCaseWithArgs<ArtistEntity>(schedulersProvider) {

    override fun run(args: ArtistEntity): Completable = repository.insertArtist(args)
}

class IsArtistSaved(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyLocalRepo
) : SingleUseCaseWithArgs<ArtistEntity, Boolean>(schedulersProvider) {

    override fun run(args: ArtistEntity): Single<Boolean> = repository.isArtistSaved(args)
}

class DeleteArtist(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyLocalRepo
) : CompletableUseCaseWithArgs<ArtistEntity>(schedulersProvider) {

    override fun run(args: ArtistEntity): Completable = repository.deleteArtist(args)
}