package com.example.there.domain.usecase.spotify

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.repo.IFavouriteTrackRepository
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.base.CompletableUseCaseWithInput
import com.example.there.domain.usecase.base.FlowableUseCase
import com.example.there.domain.usecase.base.SingleUseCaseWithInput
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class GetFavouriteTracks @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        repository: ISpotifyRepository
) : GetFavouriteTracksUseCase<TrackEntity>(schedulersProvider, repository)

class InsertTrack @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        repository: ISpotifyRepository
) : InsertTrackUseCase<TrackEntity>(schedulersProvider, repository)

class IsTrackSaved @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        repository: ISpotifyRepository
) : IsTrackSavedUseCase<TrackEntity>(schedulersProvider, repository)

class DeleteTrack @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        repository: ISpotifyRepository
) : DeleteTrackUseCase<TrackEntity>(schedulersProvider, repository)

abstract class InsertTrackUseCase<Track>(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: IFavouriteTrackRepository<Track>
) : CompletableUseCaseWithInput<Track>(schedulersProvider) {
    override fun createCompletable(input: Track): Completable = repository.insertTrack(input)
}

abstract class IsTrackSavedUseCase<Track>(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: IFavouriteTrackRepository<Track>
) : SingleUseCaseWithInput<Track, Boolean>(schedulersProvider) {
    override fun createSingle(input: Track): Single<Boolean> = repository.isTrackSaved(input)
}

abstract class DeleteTrackUseCase<Track>(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: IFavouriteTrackRepository<Track>
) : CompletableUseCaseWithInput<Track>(schedulersProvider) {
    override fun createCompletable(input: Track): Completable = repository.deleteTrack(input)
}

abstract class GetFavouriteTracksUseCase<Track>(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: IFavouriteTrackRepository<Track>
) : FlowableUseCase<List<Track>>(schedulersProvider) {
    override val flowable: Flowable<List<Track>>
        get() = repository.favouriteTracks
}
