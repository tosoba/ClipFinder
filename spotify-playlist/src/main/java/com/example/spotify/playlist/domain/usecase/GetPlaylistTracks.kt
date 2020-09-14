package com.example.spotify.playlist.domain.usecase

import com.example.core.ext.RxSchedulers
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.spotify.playlist.domain.repo.ISpotifyPlaylistRepo
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetPlaylistTracks(
    schedulers: RxSchedulers,
    private val repo: ISpotifyPlaylistRepo
) : SingleUseCaseWithArgs<GetPlaylistTracks.Args, Resource<Paged<List<TrackEntity>>>>(schedulers) {

    class Args(val playlistId: String, val offset: Int)

    override fun run(
        args: Args
    ): Single<Resource<Paged<List<TrackEntity>>>> = repo.getPlaylistTracks(
        args.playlistId,
        args.offset
    )
}
