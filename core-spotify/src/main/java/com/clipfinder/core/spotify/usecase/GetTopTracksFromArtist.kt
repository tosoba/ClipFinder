package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.spotify.auth.ISpotifyAuth
import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.example.core.ext.RxSchedulers
import com.example.core.model.Resource
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetTopTracksFromArtist(
    schedulers: RxSchedulers,
    private val auth: ISpotifyAuth,
    private val repo: ISpotifyRepo
) : SingleUseCaseWithArgs<String, Resource<List<ISpotifyTrack>>>(schedulers) {
    override fun run(args: String): Single<Resource<List<ISpotifyTrack>>> = auth
        .authorize()
        .andThen(repo.getTopTracksFromArtist(args))
}
