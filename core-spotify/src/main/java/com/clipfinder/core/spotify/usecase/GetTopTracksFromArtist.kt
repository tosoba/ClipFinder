package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.model.Resource
import com.clipfinder.core.spotify.auth.ISpotifyAutoAuth
import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.clipfinder.core.model.UseCaseWithArgs
import io.reactivex.Single

class GetTopTracksFromArtist(
    private val auth: ISpotifyAutoAuth,
    private val repo: ISpotifyRepo
) : UseCaseWithArgs<String, Single<Resource<List<ISpotifyTrack>>>> {
    override fun run(args: String): Single<Resource<List<ISpotifyTrack>>> = auth
        .authorize()
        .andThen(repo.getTopTracksFromArtist(args))
}
