package com.example.spotifyalbum.domain.usecase

import com.example.core.model.Resource
import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.Page
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetTracksFromAlbum(
    schedulersProvider: UseCaseSchedulersProvider,
    private val remote: ISpotifyRemoteDataStore
) : SingleUseCaseWithArgs<GetTracksFromAlbum.Args, Resource<Page<TrackEntity>>>(
    schedulersProvider
) {
    override fun run(
        args: Args
    ): Single<Resource<Page<TrackEntity>>> = remote.getTracksFromAlbum(
        args.albumId,
        args.offset
    )

    class Args(val albumId: String, val offset: Int)
}
