package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.model.Paged
import com.clipfinder.core.model.Resource
import com.clipfinder.core.spotify.model.ISpotifySimplifiedAlbum
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.clipfinder.core.model.UseCaseWithArgs
import io.reactivex.Single

class GetCurrentUsersSavedAlbums(
    private val repo: ISpotifyRepo
) : UseCaseWithArgs<Int, Single<Resource<Paged<List<ISpotifySimplifiedAlbum>>>>> {
    override fun run(args: Int): Single<Resource<Paged<List<ISpotifySimplifiedAlbum>>>> = repo
        .getCurrentUsersSavedAlbums(offset = args)
}
