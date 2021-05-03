package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.model.Paged
import com.clipfinder.core.model.Resource
import com.clipfinder.core.model.UseCaseWithArgs
import com.clipfinder.core.spotify.model.ISpotifyArtist
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import io.reactivex.Single

class GetCurrentUsersTopArtists(private val repo: ISpotifyRepo) :
    UseCaseWithArgs<Int, Single<Resource<Paged<List<ISpotifyArtist>>>>> {
    override fun run(args: Int): Single<Resource<Paged<List<ISpotifyArtist>>>> =
        repo.getCurrentUsersTopArtists(offset = args)
}
