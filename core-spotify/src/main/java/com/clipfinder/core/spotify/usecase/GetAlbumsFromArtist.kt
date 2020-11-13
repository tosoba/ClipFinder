package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.spotify.auth.ISpotifyAuth
import com.clipfinder.core.spotify.model.ISpotifySimplifiedAlbum
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.example.core.ext.RxSchedulers
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetAlbumsFromArtist(
    schedulers: RxSchedulers,
    private val auth: ISpotifyAuth,
    private val repo: ISpotifyRepo
) : SingleUseCaseWithArgs<GetAlbumsFromArtist.Args, Resource<Paged<List<ISpotifySimplifiedAlbum>>>>(schedulers) {

    override fun run(args: Args): Single<Resource<Paged<List<ISpotifySimplifiedAlbum>>>> = auth
        .authorize()
        .andThen(repo.getAlbumsFromArtist(artistId = args.artistId, offset = args.offset))

    class Args(val artistId: String, val offset: Int)
}
