package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.model.Resource
import com.clipfinder.core.model.UseCaseWithArgs
import com.clipfinder.core.spotify.model.ISpotifyAudioFeatures
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import io.reactivex.Single

class GetAudioFeatures(private val repo: ISpotifyRepo) :
    UseCaseWithArgs<String, Single<Resource<ISpotifyAudioFeatures>>> {
    override fun run(args: String): Single<Resource<ISpotifyAudioFeatures>> =
        repo.getAudioFeatures(args)
}
