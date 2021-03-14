package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.model.Paged
import com.clipfinder.core.model.Resource
import com.clipfinder.core.spotify.auth.ISpotifyAutoAuth
import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.clipfinder.core.model.UseCaseWithArgs
import io.reactivex.Single

class GetCurrentUsersSavedTracks(
    private val auth: ISpotifyAutoAuth,
    private val repo: ISpotifyRepo
) : UseCaseWithArgs<Int, Single<Resource<Paged<List<ISpotifyTrack>>>>> {
    override fun run(args: Int): Single<Resource<Paged<List<ISpotifyTrack>>>> = auth
        .authorizePrivate()
        .andThen(repo.getCurrentUsersSavedTracks(offset = args))
}
