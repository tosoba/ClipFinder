package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.model.Paged
import com.clipfinder.core.model.Resource
import com.clipfinder.core.spotify.auth.ISpotifyAutoAuth
import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.clipfinder.core.model.UseCaseWithArgs
import io.reactivex.Single

class GetSimilarTracks(
    private val auth: ISpotifyAutoAuth,
    private val repo: ISpotifyRepo
) : UseCaseWithArgs<GetSimilarTracks.Args, Single<Resource<Paged<List<ISpotifyTrack>>>>> {

    override fun run(args: Args): Single<Resource<Paged<List<ISpotifyTrack>>>> = auth
        .authorizePublic()
        .andThen(repo.getSimilarTracks(args.trackId, args.offset))

    class Args(val trackId: String, val offset: Int)
}
