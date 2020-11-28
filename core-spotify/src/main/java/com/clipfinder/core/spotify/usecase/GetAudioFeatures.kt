package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.spotify.model.ISpotifyAudioFeatures
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.example.core.ext.RxSchedulers
import com.example.core.model.Resource
import com.example.core.usecase.SingleUseCaseWithArgs
import io.reactivex.Single

class GetAudioFeatures(
    schedulers: RxSchedulers,
    private val repo: ISpotifyRepo
) : SingleUseCaseWithArgs<String, Resource<ISpotifyAudioFeatures>>(schedulers) {
    override fun run(args: String): Single<Resource<ISpotifyAudioFeatures>> = repo.getAudioFeatures(args)
}
