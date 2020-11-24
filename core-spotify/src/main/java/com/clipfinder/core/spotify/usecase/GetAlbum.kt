package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.spotify.auth.ISpotifyAutoAuth
import com.clipfinder.core.spotify.model.ISpotifySimplifiedAlbum
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.example.core.ext.RxSchedulers
import com.example.core.model.Resource
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetAlbum(
    schedulers: RxSchedulers,
    private val auth: ISpotifyAutoAuth,
    private val remote: ISpotifyRepo
) : SingleUseCaseWithArgs<String, Resource<ISpotifySimplifiedAlbum>>(schedulers) {
    override fun run(args: String): Single<Resource<ISpotifySimplifiedAlbum>> = auth
        .authorize()
        .andThen(remote.getAlbum(id = args))
}
