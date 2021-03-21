package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.model.Resource
import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.clipfinder.core.model.UseCaseWithArgs
import io.reactivex.Single

class GetTrack(
    private val repo: ISpotifyRepo
) : UseCaseWithArgs<String, Single<Resource<ISpotifyTrack>>> {
    override fun run(args: String): Single<Resource<ISpotifyTrack>> = repo.getTrack(id = args)
}
