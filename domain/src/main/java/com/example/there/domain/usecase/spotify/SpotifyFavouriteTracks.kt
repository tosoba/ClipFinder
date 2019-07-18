package com.example.there.domain.usecase.spotify

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.repo.spotify.ISpotifyLocalRepo
import com.example.there.domain.usecase.base.DeleteTrackUseCase
import com.example.there.domain.usecase.base.GetFavouriteTracksUseCase
import com.example.there.domain.usecase.base.InsertTrackUseCase
import com.example.there.domain.usecase.base.IsTrackSavedUseCase

class GetFavouriteTracks(
        schedulersProvider: UseCaseSchedulersProvider,
        repository: ISpotifyLocalRepo
) : GetFavouriteTracksUseCase<TrackEntity>(schedulersProvider, repository)

class InsertTrack(
        schedulersProvider: UseCaseSchedulersProvider,
        repository: ISpotifyLocalRepo
) : InsertTrackUseCase<TrackEntity>(schedulersProvider, repository)

class IsTrackSaved(
        schedulersProvider: UseCaseSchedulersProvider,
        repository: ISpotifyLocalRepo
) : IsTrackSavedUseCase<TrackEntity>(schedulersProvider, repository)

class DeleteTrack(
        schedulersProvider: UseCaseSchedulersProvider,
        repository: ISpotifyLocalRepo
) : DeleteTrackUseCase<TrackEntity>(schedulersProvider, repository)

