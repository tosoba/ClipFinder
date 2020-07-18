package com.example.there.domain.usecase.spotify

import com.example.core.ext.RxSchedulers
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.repo.spotify.ISpotifyLocalRepo
import com.example.there.domain.usecase.base.DeleteTrackUseCase
import com.example.there.domain.usecase.base.GetFavouriteTracksUseCase
import com.example.there.domain.usecase.base.InsertTrackUseCase
import com.example.there.domain.usecase.base.IsTrackSavedUseCase

class GetFavouriteTracks(
    schedulers: RxSchedulers,
    repository: ISpotifyLocalRepo
) : GetFavouriteTracksUseCase<TrackEntity>(schedulers, repository)

class InsertTrack(
    schedulers: RxSchedulers,
    repository: ISpotifyLocalRepo
) : InsertTrackUseCase<TrackEntity>(schedulers, repository)

class IsTrackSaved(
    schedulers: RxSchedulers,
    repository: ISpotifyLocalRepo
) : IsTrackSavedUseCase<TrackEntity>(schedulers, repository)

class DeleteTrack(
    schedulers: RxSchedulers,
    repository: ISpotifyLocalRepo
) : DeleteTrackUseCase<TrackEntity>(schedulers, repository)
