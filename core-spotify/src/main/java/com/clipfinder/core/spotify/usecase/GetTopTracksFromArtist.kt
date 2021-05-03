package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.model.Resource
import com.clipfinder.core.model.UseCaseWithArgs
import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import io.reactivex.Single

class GetTopTracksFromArtist(private val repo: ISpotifyRepo) :
    UseCaseWithArgs<String, Single<Resource<List<ISpotifyTrack>>>> {
    override fun run(args: String): Single<Resource<List<ISpotifyTrack>>> =
        repo.getTopTracksFromArtist(args)
}
