package com.example.there.domain.usecase.spotify

import com.example.core.model.Resource
import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.ListPage
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single
import javax.inject.Inject

class GetPlaylistTracks @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val remote: ISpotifyRemoteDataStore
) : SingleUseCaseWithArgs<GetPlaylistTracks.Args, Resource<ListPage<TrackEntity>>>(schedulersProvider) {

    class Args(
            val playlistId: String,
            val userId: String,
            val offset: Int
    )

    override fun run(args: Args): Single<Resource<ListPage<TrackEntity>>> =
            remote.getPlaylistTracks(args.playlistId, args.userId, args.offset)
}