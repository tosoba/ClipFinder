package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.model.Resource
import com.clipfinder.core.spotify.auth.ISpotifyAutoAuth
import com.clipfinder.core.spotify.model.ISpotifyArtist
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.clipfinder.core.model.UseCaseWithArgs
import io.reactivex.Single

class GetArtists(
    private val auth: ISpotifyAutoAuth,
    private val repo: ISpotifyRepo
) : UseCaseWithArgs<List<String>, Single<Resource<List<ISpotifyArtist>>>> {
    override fun run(args: List<String>): Single<Resource<List<ISpotifyArtist>>> = auth
        .authorizePublic()
        .andThen(repo.getArtists(ids = args))
}
