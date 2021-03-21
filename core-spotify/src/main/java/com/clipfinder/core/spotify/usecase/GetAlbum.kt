package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.model.Resource
import com.clipfinder.core.model.UseCaseWithArgs
import com.clipfinder.core.spotify.model.ISpotifySimplifiedAlbum
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import io.reactivex.Single

class GetAlbum(private val remote: ISpotifyRepo) : UseCaseWithArgs<String, Single<Resource<ISpotifySimplifiedAlbum>>> {
    override fun run(args: String): Single<Resource<ISpotifySimplifiedAlbum>> = remote.getAlbum(id = args)
}
