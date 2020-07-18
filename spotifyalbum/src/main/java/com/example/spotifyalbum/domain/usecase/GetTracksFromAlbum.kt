package com.example.spotifyalbum.domain.usecase

import com.example.core.ext.RxSchedulers
import com.example.core.model.Resource
import com.example.spotifyalbum.domain.repo.ISpotifyAlbumRepo
import com.example.there.domain.entity.Page
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetTracksFromAlbum(
    schedulers: RxSchedulers,
    private val repo: ISpotifyAlbumRepo
) : SingleUseCaseWithArgs<GetTracksFromAlbum.Args, Resource<Page<TrackEntity>>>(schedulers) {

    override fun run(
        args: Args
    ): Single<Resource<Page<TrackEntity>>> = repo.getTracksFromAlbum(
        args.albumId,
        args.offset
    )

    class Args(val albumId: String, val offset: Int)
}
