package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.ext.RxSchedulers
import com.clipfinder.core.model.Resource
import com.clipfinder.core.spotify.auth.ISpotifyAutoAuth
import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.clipfinder.core.usecase.SingleUseCaseWithArgs
import io.reactivex.Single

class GetTrack(
    schedulers: RxSchedulers,
    private val auth: ISpotifyAutoAuth,
    private val repo: ISpotifyRepo
) : SingleUseCaseWithArgs<String, Resource<ISpotifyTrack>>(schedulers) {
    override fun run(args: String): Single<Resource<ISpotifyTrack>> = auth
        .authorize()
        .andThen(repo.getTrack(id = args))
}
