package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.spotify.auth.ISpotifyAutoAuth
import com.clipfinder.core.spotify.model.ISpotifyArtist
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.example.core.ext.RxSchedulers
import com.example.core.model.Resource
import com.example.core.usecase.SingleUseCaseWithArgs
import io.reactivex.Single

class GetArtists(
    schedulers: RxSchedulers,
    private val auth: ISpotifyAutoAuth,
    private val repo: ISpotifyRepo
) : SingleUseCaseWithArgs<List<String>, Resource<List<ISpotifyArtist>>>(schedulers) {
    override fun run(args: List<String>): Single<Resource<List<ISpotifyArtist>>> = auth
        .authorize()
        .andThen(repo.getArtists(ids = args))
}
