package com.example.there.domain.usecase.spotify

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.EntityPage
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single
import javax.inject.Inject

class GetPlaylistTracks @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val remote: ISpotifyRemoteDataStore
) : SingleUseCaseWithArgs<GetPlaylistTracks.Args, EntityPage<TrackEntity>>(schedulersProvider) {

    class Args(
            val playlistId: String,
            val userId: String,
            val offset: Int
    )

    override fun createSingle(args: Args): Single<EntityPage<TrackEntity>> = remote.getPlaylistTracks(args.playlistId, args.userId, args.offset)
}