package com.example.there.domain.usecase.spotify

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.base.CompletableUseCaseWithInput
import com.example.there.domain.usecase.base.FlowableUseCase
import com.example.there.domain.usecase.base.SingleUseCaseWithInput
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class GetFavouriteArtists @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyRepository
) : FlowableUseCase<List<ArtistEntity>>(schedulersProvider) {

    override val flowable: Flowable<List<ArtistEntity>>
        get() = repository.favouriteArtists
}

class InsertArtist @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyRepository
) : CompletableUseCaseWithInput<ArtistEntity>(schedulersProvider) {

    override fun createCompletable(input: ArtistEntity): Completable = repository.insertArtist(input)
}

class IsArtistSaved @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyRepository
) : SingleUseCaseWithInput<ArtistEntity, Boolean>(schedulersProvider) {

    override fun createSingle(input: ArtistEntity): Single<Boolean> = repository.isArtistSaved(input)
}

class DeleteArtist @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyRepository
) : CompletableUseCaseWithInput<ArtistEntity>(schedulersProvider) {

    override fun createCompletable(input: ArtistEntity): Completable = repository.deleteArtist(input)
}