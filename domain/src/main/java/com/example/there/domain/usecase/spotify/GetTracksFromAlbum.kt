package com.example.there.domain.usecase.spotify

import com.example.core.model.Resource
import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.ListPage
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetTracksFromAlbum(
        schedulersProvider: UseCaseSchedulersProvider,
        private val remote: ISpotifyRemoteDataStore
) : SingleUseCaseWithArgs<GetTracksFromAlbum.Args, Resource<ListPage<TrackEntity>>>(schedulersProvider) {

    override fun run(args: Args): Single<Resource<ListPage<TrackEntity>>> = remote.getTracksFromAlbum(args.albumId, args.offset)

    class Args(val albumId: String, val offset: Int)
}