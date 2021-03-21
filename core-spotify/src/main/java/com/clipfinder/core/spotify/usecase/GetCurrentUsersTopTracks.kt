package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.model.Paged
import com.clipfinder.core.model.Resource
import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.clipfinder.core.model.UseCaseWithArgs
import io.reactivex.Single

class GetCurrentUsersTopTracks(
    private val remote: ISpotifyRepo
) : UseCaseWithArgs<Int, Single<Resource<Paged<List<ISpotifyTrack>>>>> {
    override fun run(args: Int): Single<Resource<Paged<List<ISpotifyTrack>>>> = remote
        .getCurrentUsersTopTracks(offset = args)
}
