package com.example.there.domain.usecase.spotify

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.repo.spotify.ISpotifyLocalRepo
import com.example.there.domain.usecase.base.DeletePlaylistUseCase
import com.example.there.domain.usecase.base.GetFavouritePlaylistsUseCase
import com.example.there.domain.usecase.base.InsertPlaylistUseCase
import com.example.there.domain.usecase.base.IsPlaylistSavedUseCase
import javax.inject.Inject

class GetFavouriteSpotifyPlaylists @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        repository: ISpotifyLocalRepo
) : GetFavouritePlaylistsUseCase<PlaylistEntity>(schedulersProvider, repository)

class InsertSpotifyPlaylist @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        repository: ISpotifyLocalRepo
) : InsertPlaylistUseCase<PlaylistEntity>(schedulersProvider, repository)

class IsSpotifyPlaylistSaved @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        repository: ISpotifyLocalRepo
) : IsPlaylistSavedUseCase<PlaylistEntity>(schedulersProvider, repository)

class DeleteSpotifyPlaylist @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        repository: ISpotifyLocalRepo
) : DeletePlaylistUseCase<PlaylistEntity>(schedulersProvider, repository)