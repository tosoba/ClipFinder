package com.example.spotify.playlist.domain.usecase

import com.clipfinder.core.spotify.auth.ISpotifyAuth
import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.example.core.ext.RxSchedulers
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.spotify.playlist.domain.repo.ISpotifyPlaylistRepo
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetPlaylistTracks(
    schedulers: RxSchedulers,
    private val auth: ISpotifyAuth,
    private val repo: ISpotifyPlaylistRepo
) : SingleUseCaseWithArgs<GetPlaylistTracks.Args, Resource<Paged<List<ISpotifyTrack>>>>(schedulers) {

    override fun run(args: Args): Single<Resource<Paged<List<ISpotifyTrack>>>> = auth
        .authorize()
        .andThen(repo.getPlaylistTracks(args.playlistId, args.offset))

    class Args(val playlistId: String, val offset: Int)
}
