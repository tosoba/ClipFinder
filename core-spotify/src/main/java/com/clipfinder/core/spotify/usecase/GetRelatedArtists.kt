package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.spotify.auth.ISpotifyAuth
import com.clipfinder.core.spotify.model.ISpotifyArtist
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.example.core.ext.RxSchedulers
import com.example.core.model.Resource
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetRelatedArtists(
    schedulers: RxSchedulers,
    private val auth: ISpotifyAuth,
    private val repo: ISpotifyRepo
) : SingleUseCaseWithArgs<String, Resource<List<ISpotifyArtist>>>(schedulers) {
    override fun run(args: String): Single<Resource<List<ISpotifyArtist>>> = auth
        .authorize()
        .andThen(repo.getRelatedArtists(args))
}
