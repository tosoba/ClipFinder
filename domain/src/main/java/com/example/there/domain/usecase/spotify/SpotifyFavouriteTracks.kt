package com.example.there.domain.usecase.spotify

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.base.DeleteTrackUseCase
import com.example.there.domain.usecase.base.GetFavouriteTracksUseCase
import com.example.there.domain.usecase.base.InsertTrackUseCase
import com.example.there.domain.usecase.base.IsTrackSavedUseCase
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

