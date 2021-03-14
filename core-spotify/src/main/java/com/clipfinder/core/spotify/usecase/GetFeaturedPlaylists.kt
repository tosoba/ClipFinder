package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.model.Paged
import com.clipfinder.core.model.Resource
import com.clipfinder.core.spotify.auth.ISpotifyAutoAuth
import com.clipfinder.core.spotify.model.ISpotifySimplifiedPlaylist
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.clipfinder.core.model.UseCaseWithArgs
import io.reactivex.Single

class GetFeaturedPlaylists(
    private val auth: ISpotifyAutoAuth,
    private val remote: ISpotifyRepo
) : UseCaseWithArgs<Int, Single<Resource<Paged<List<ISpotifySimplifiedPlaylist>>>>> {
    override fun run(args: Int): Single<Resource<Paged<List<ISpotifySimplifiedPlaylist>>>> = auth
        .authorizePublic()
        .andThen(remote.getFeaturedPlaylists(offset = args))
}
