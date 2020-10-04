package com.example.spotify.account.top.domain.usecase

import com.clipfinder.core.spotify.auth.ISpotifyAuth
import com.clipfinder.core.spotify.model.ISpotifyArtist
import com.example.core.ext.RxSchedulers
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.spotify.account.top.domain.repo.ISpotifyAccountTopRepo
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetCurrentUsersTopArtists(
    schedulers: RxSchedulers,
    private val auth: ISpotifyAuth,
    private val repo: ISpotifyAccountTopRepo
) : SingleUseCaseWithArgs<Int, Resource<Paged<List<ISpotifyArtist>>>>(schedulers) {
    override fun run(args: Int): Single<Resource<Paged<List<ISpotifyArtist>>>> = auth
        .requirePrivateAuthorized()
        .andThen(repo.getCurrentUsersTopArtists(offset = args))
}
