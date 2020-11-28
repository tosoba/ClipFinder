package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.ext.RxSchedulers
import com.clipfinder.core.model.Paged
import com.clipfinder.core.model.Resource
import com.clipfinder.core.spotify.auth.ISpotifyAutoAuth
import com.clipfinder.core.spotify.model.ISpotifySimplifiedAlbum
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.clipfinder.core.usecase.SingleUseCaseWithArgs
import io.reactivex.Single

class GetCurrentUsersSavedAlbums(
    schedulers: RxSchedulers,
    private val auth: ISpotifyAutoAuth,
    private val repo: ISpotifyRepo
) : SingleUseCaseWithArgs<Int, Resource<Paged<List<ISpotifySimplifiedAlbum>>>>(schedulers) {
    override fun run(args: Int): Single<Resource<Paged<List<ISpotifySimplifiedAlbum>>>> = auth
        .requirePrivateAuthorized()
        .andThen(repo.getCurrentUsersSavedAlbums(offset = args))
}
