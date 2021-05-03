package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.model.Paged
import com.clipfinder.core.model.Resource
import com.clipfinder.core.model.UseCaseWithArgs
import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import io.reactivex.Single

class GetCurrentUsersSavedTracks(private val repo: ISpotifyRepo) :
    UseCaseWithArgs<Int, Single<Resource<Paged<List<ISpotifyTrack>>>>> {
    override fun run(args: Int): Single<Resource<Paged<List<ISpotifyTrack>>>> =
        repo.getCurrentUsersSavedTracks(offset = args)
}
