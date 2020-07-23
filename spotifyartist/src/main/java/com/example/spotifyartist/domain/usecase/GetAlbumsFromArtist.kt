package com.example.spotifyartist.domain.usecase

import com.example.core.ext.RxSchedulers
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.spotifyartist.domain.repo.ISpotifyArtistRepo
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetAlbumsFromArtist(
    schedulers: RxSchedulers,
    private val repo: ISpotifyArtistRepo
) : SingleUseCaseWithArgs<GetAlbumsFromArtist.Args, Resource<Paged<List<AlbumEntity>>>>(schedulers) {
    class Args(val artistId: String, val offset: Int)

    override fun run(
        args: Args
    ): Single<Resource<Paged<List<AlbumEntity>>>> = repo.getAlbumsFromArtist(
        artistId = args.artistId,
        offset = args.offset
    )
}
