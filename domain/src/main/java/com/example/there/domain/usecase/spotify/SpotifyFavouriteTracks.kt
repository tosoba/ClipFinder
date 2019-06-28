package com.example.there.domain.usecase.spotify

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.repo.spotify.ISpotifyLocalRepo
import com.example.there.domain.usecase.base.DeleteTrackUseCase
import com.example.there.domain.usecase.base.GetFavouriteTracksUseCase
import com.example.there.domain.usecase.base.InsertTrackUseCase
import com.example.there.domain.usecase.base.IsTrackSavedUseCase
import javax.inject.Inject

class GetFavouriteTracks @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        repository: ISpotifyLocalRepo
) : GetFavouriteTracksUseCase<TrackEntity>(schedulersProvider, repository)

class InsertTrack @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        repository: ISpotifyLocalRepo
) : InsertTrackUseCase<TrackEntity>(schedulersProvider, repository)

class IsTrackSaved @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        repository: ISpotifyLocalRepo
) : IsTrackSavedUseCase<TrackEntity>(schedulersProvider, repository)

class DeleteTrack @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        repository: ISpotifyLocalRepo
) : DeleteTrackUseCase<TrackEntity>(schedulersProvider, repository)

