package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.model.Resource
import com.clipfinder.core.model.UseCaseWithArgs
import com.clipfinder.core.spotify.model.ISpotifyArtist
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import io.reactivex.Single

class GetRelatedArtists(private val repo: ISpotifyRepo) :
    UseCaseWithArgs<String, Single<Resource<List<ISpotifyArtist>>>> {
    override fun run(args: String): Single<Resource<List<ISpotifyArtist>>> =
        repo.getRelatedArtists(args)
}
