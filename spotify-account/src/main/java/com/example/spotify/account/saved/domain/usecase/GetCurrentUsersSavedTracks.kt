package com.example.spotify.account.saved.domain.usecase

import com.clipfinder.core.spotify.auth.ISpotifyAuth
import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.example.core.ext.RxSchedulers
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.spotify.account.saved.domain.repo.ISpotifyAccountSavedRepo
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetCurrentUsersSavedTracks(
    schedulers: RxSchedulers,
    private val auth: ISpotifyAuth,
    private val repo: ISpotifyAccountSavedRepo
) : SingleUseCaseWithArgs<Int, Resource<Paged<List<ISpotifyTrack>>>>(schedulers) {
    override fun run(args: Int): Single<Resource<Paged<List<ISpotifyTrack>>>> = auth
        .requirePrivateAuthorized()
        .andThen(repo.getCurrentUsersSavedTracks(offset = args))
}
