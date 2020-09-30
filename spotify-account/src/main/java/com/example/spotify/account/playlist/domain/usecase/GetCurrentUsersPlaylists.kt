package com.example.spotify.account.playlist.domain.usecase

import com.clipfinder.core.spotify.auth.ISpotifyAuth
import com.clipfinder.core.spotify.model.ISpotifySimplePlaylist
import com.example.core.ext.RxSchedulers
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.spotify.account.playlist.domain.repo.ISpotifyAccountPlaylistsRepo
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetCurrentUsersPlaylists(
    schedulers: RxSchedulers,
    private val auth: ISpotifyAuth,
    private val repo: ISpotifyAccountPlaylistsRepo
) : SingleUseCaseWithArgs<Int, Resource<Paged<List<ISpotifySimplePlaylist>>>>(schedulers) {
    override fun run(args: Int): Single<Resource<Paged<List<ISpotifySimplePlaylist>>>> = auth
        .requirePrivateAuthorized()
        .andThen(repo.getCurrentUsersPlaylists(offset = args))
}
