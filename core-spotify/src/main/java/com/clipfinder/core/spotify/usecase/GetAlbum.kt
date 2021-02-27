package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.model.Resource
import com.clipfinder.core.spotify.auth.ISpotifyAutoAuth
import com.clipfinder.core.spotify.model.ISpotifySimplifiedAlbum
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.clipfinder.core.model.UseCaseWithArgs
import io.reactivex.Single

class GetAlbum(
    private val auth: ISpotifyAutoAuth,
    private val remote: ISpotifyRepo
) : UseCaseWithArgs<String, Single<Resource<ISpotifySimplifiedAlbum>>> {
    override fun run(args: String): Single<Resource<ISpotifySimplifiedAlbum>> = auth
        .authorize()
        .andThen(remote.getAlbum(id = args))
}
