package com.example.there.domain.usecase.spotify

import com.example.core.model.Resource
import com.example.core.ext.RxSchedulers
import com.example.there.domain.entity.Page
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetPlaylistTracks(
    schedulers: RxSchedulers,
    private val remote: ISpotifyRemoteDataStore
) : SingleUseCaseWithArgs<GetPlaylistTracks.Args, Resource<Page<TrackEntity>>>(schedulers) {

    class Args(
        val playlistId: String,
        val userId: String,
        val offset: Int
    )

    override fun run(
        args: Args
    ): Single<Resource<Page<TrackEntity>>> = remote.getPlaylistTracks(
        args.playlistId,
        args.userId,
        args.offset
    )
}
