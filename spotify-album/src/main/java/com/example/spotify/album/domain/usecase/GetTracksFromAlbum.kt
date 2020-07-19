package com.example.spotify.album.domain.usecase

import com.example.core.ext.RxSchedulers
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.spotify.album.domain.repo.ISpotifyAlbumRepo
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetTracksFromAlbum(
    schedulers: RxSchedulers,
    private val repo: ISpotifyAlbumRepo
) : SingleUseCaseWithArgs<GetTracksFromAlbum.Args, Resource<Paged<List<TrackEntity>>>>(schedulers) {

    override fun run(
        args: Args
    ): Single<Resource<Paged<List<TrackEntity>>>> = repo.getTracksFromAlbum(
        albumId = args.albumId,
        offset = args.offset
    )

    class Args(val albumId: String, val offset: Int)
}
