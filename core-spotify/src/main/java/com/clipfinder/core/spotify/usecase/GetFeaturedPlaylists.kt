package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.spotify.auth.ISpotifyAutoAuth
import com.clipfinder.core.spotify.model.ISpotifySimplifiedPlaylist
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.example.core.ext.RxSchedulers
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetFeaturedPlaylists(
    schedulers: RxSchedulers,
    private val auth: ISpotifyAutoAuth,
    private val remote: ISpotifyRepo
) : SingleUseCaseWithArgs<Int, Resource<Paged<List<ISpotifySimplifiedPlaylist>>>>(schedulers) {
    override fun run(args: Int): Single<Resource<Paged<List<ISpotifySimplifiedPlaylist>>>> = auth
        .authorize()
        .andThen(remote.getFeaturedPlaylists(offset = args))
}
