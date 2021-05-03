package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.model.Paged
import com.clipfinder.core.model.Resource
import com.clipfinder.core.model.UseCaseWithArgs
import com.clipfinder.core.spotify.model.ISpotifySimplifiedPlaylist
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import io.reactivex.Single

class GetFeaturedPlaylists(private val remote: ISpotifyRepo) :
    UseCaseWithArgs<Int, Single<Resource<Paged<List<ISpotifySimplifiedPlaylist>>>>> {
    override fun run(args: Int): Single<Resource<Paged<List<ISpotifySimplifiedPlaylist>>>> =
        remote.getFeaturedPlaylists(offset = args)
}
