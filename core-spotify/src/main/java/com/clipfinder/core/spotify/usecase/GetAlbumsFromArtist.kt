package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.model.Paged
import com.clipfinder.core.model.Resource
import com.clipfinder.core.spotify.model.ISpotifySimplifiedAlbum
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.clipfinder.core.model.UseCaseWithArgs
import io.reactivex.Single

class GetAlbumsFromArtist(
    private val repo: ISpotifyRepo
) : UseCaseWithArgs<GetAlbumsFromArtist.Args, Single<Resource<Paged<List<ISpotifySimplifiedAlbum>>>>> {
    override fun run(args: Args): Single<Resource<Paged<List<ISpotifySimplifiedAlbum>>>> = repo
        .getAlbumsFromArtist(artistId = args.artistId, offset = args.offset)

    class Args(val artistId: String, val offset: Int)
}
