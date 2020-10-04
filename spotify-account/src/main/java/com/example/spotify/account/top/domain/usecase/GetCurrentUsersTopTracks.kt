package com.example.spotify.account.top.domain.usecase

import com.clipfinder.core.spotify.auth.ISpotifyAuth
import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.example.core.ext.RxSchedulers
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.spotify.account.top.domain.repo.ISpotifyAccountTopRepo
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetCurrentUsersTopTracks(
    schedulers: RxSchedulers,
    private val auth: ISpotifyAuth,
    private val remote: ISpotifyAccountTopRepo
) : SingleUseCaseWithArgs<Int, Resource<Paged<List<ISpotifyTrack>>>>(schedulers) {
    override fun run(args: Int): Single<Resource<Paged<List<ISpotifyTrack>>>> = auth
        .requirePrivateAuthorized()
        .andThen(remote.getCurrentUsersTopTracks(offset = args))
}
