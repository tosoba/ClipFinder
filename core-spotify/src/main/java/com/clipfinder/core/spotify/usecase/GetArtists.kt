package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.model.Resource
import com.clipfinder.core.model.UseCaseWithArgs
import com.clipfinder.core.spotify.model.ISpotifyArtist
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import io.reactivex.Single

class GetArtists(private val repo: ISpotifyRepo) :
    UseCaseWithArgs<List<String>, Single<Resource<List<ISpotifyArtist>>>> {
    override fun run(args: List<String>): Single<Resource<List<ISpotifyArtist>>> =
        repo.getArtists(ids = args)
}
