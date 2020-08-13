package com.example.spotify.account.playlist.domain.usecase

import com.example.core.ext.RxSchedulers
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.spotify.account.playlist.domain.repo.ISpotifyAccountPlaylistsRepo
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetCurrentUsersPlaylists(
    schedulers: RxSchedulers,
    private val repo: ISpotifyAccountPlaylistsRepo
) : SingleUseCaseWithArgs<Int, Resource<Paged<List<PlaylistEntity>>>>(schedulers) {
    override fun run(args: Int): Single<Resource<Paged<List<PlaylistEntity>>>> = repo
        .getCurrentUsersPlaylists(offset = args)
}
