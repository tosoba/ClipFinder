package com.example.spotify.dashboard.domain.usecase

import com.clipfinder.core.spotify.auth.ISpotifyAuth
import com.clipfinder.core.spotify.model.ISpotifySimplePlaylist
import com.example.core.ext.RxSchedulers
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.spotify.dashboard.domain.repo.ISpotifyDashboardRepo
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetFeaturedPlaylists(
    schedulers: RxSchedulers,
    private val auth: ISpotifyAuth,
    private val remote: ISpotifyDashboardRepo
) : SingleUseCaseWithArgs<Int, Resource<Paged<List<ISpotifySimplePlaylist>>>>(schedulers) {
    override fun run(args: Int): Single<Resource<Paged<List<ISpotifySimplePlaylist>>>> = auth
        .authorize()
        .andThen(remote.getFeaturedPlaylists(offset = args))
}
