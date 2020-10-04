package com.example.spotify.account.saved.domain.usecase

import com.clipfinder.core.spotify.auth.ISpotifyAuth
import com.clipfinder.core.spotify.model.ISpotifySimplifiedAlbum
import com.example.core.ext.RxSchedulers
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.spotify.account.saved.domain.repo.ISpotifyAccountSavedRepo
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetCurrentUsersSavedAlbums(
    schedulers: RxSchedulers,
    private val auth: ISpotifyAuth,
    private val repo: ISpotifyAccountSavedRepo
) : SingleUseCaseWithArgs<Int, Resource<Paged<List<ISpotifySimplifiedAlbum>>>>(schedulers) {
    override fun run(args: Int): Single<Resource<Paged<List<ISpotifySimplifiedAlbum>>>> = auth
        .requirePrivateAuthorized()
        .andThen(repo.getCurrentUsersSavedAlbums(offset = args))
}
