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
import javax.inject.Inject

class GetFavouriteArtists @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyLocalRepo
) : FlowableUseCase<List<ArtistEntity>>(schedulersProvider) {

    override val flowable: Flowable<List<ArtistEntity>>
        get() = repository.favouriteArtists
}

class InsertArtist @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyLocalRepo
) : CompletableUseCaseWithArgs<ArtistEntity>(schedulersProvider) {

    override fun createCompletable(args: ArtistEntity): Completable = repository.insertArtist(args)
}

class IsArtistSaved @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyLocalRepo
) : SingleUseCaseWithArgs<ArtistEntity, Boolean>(schedulersProvider) {

    override fun createSingle(args: ArtistEntity): Single<Boolean> = repository.isArtistSaved(args)
}

class DeleteArtist @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyLocalRepo
) : CompletableUseCaseWithArgs<ArtistEntity>(schedulersProvider) {

    override fun createCompletable(args: ArtistEntity): Completable = repository.deleteArtist(args)
}