package com.example.there.domain.usecase.spotify

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.base.DeletePlaylistUseCase
import com.example.there.domain.usecase.base.GetFavouritePlaylistsUseCase
import com.example.there.domain.usecase.base.InsertPlaylistUseCase
import com.example.there.domain.usecase.base.IsPlaylistSavedUseCase
import javax.inject.Inject

class GetFavouriteSpotifyPlaylists @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        repository: ISpotifyRepository
) : GetFavouritePlaylistsUseCase<PlaylistEntity>(schedulersProvider, repository)

class InsertSpotifyPlaylist @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        repository: ISpotifyRepository
) : InsertPlaylistUseCase<PlaylistEntity>(schedulersProvider, repository)

class IsSpotifyPlaylistSaved @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        repository: ISpotifyRepository
) : IsPlaylistSavedUseCase<PlaylistEntity>(schedulersProvider, repository)

class DeleteSpotifyPlaylist @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        repository: ISpotifyRepository
) : DeletePlaylistUseCase<PlaylistEntity>(schedulersProvider, repository)