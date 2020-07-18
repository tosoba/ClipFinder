package com.example.there.domain.usecase.spotify

import com.example.core.ext.RxSchedulers
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.repo.spotify.ISpotifyLocalRepo
import com.example.there.domain.usecase.base.DeletePlaylistUseCase
import com.example.there.domain.usecase.base.GetFavouritePlaylistsUseCase
import com.example.there.domain.usecase.base.InsertPlaylistUseCase
import com.example.there.domain.usecase.base.IsPlaylistSavedUseCase

class GetFavouriteSpotifyPlaylists(
    schedulers: RxSchedulers,
    repository: ISpotifyLocalRepo
) : GetFavouritePlaylistsUseCase<PlaylistEntity>(schedulers, repository)

class InsertSpotifyPlaylist(
    schedulers: RxSchedulers,
    repository: ISpotifyLocalRepo
) : InsertPlaylistUseCase<PlaylistEntity>(schedulers, repository)

class IsSpotifyPlaylistSaved(
    schedulers: RxSchedulers,
    repository: ISpotifyLocalRepo
) : IsPlaylistSavedUseCase<PlaylistEntity>(schedulers, repository)

class DeleteSpotifyPlaylist(
    schedulers: RxSchedulers,
    repository: ISpotifyLocalRepo
) : DeletePlaylistUseCase<PlaylistEntity>(schedulers, repository)
