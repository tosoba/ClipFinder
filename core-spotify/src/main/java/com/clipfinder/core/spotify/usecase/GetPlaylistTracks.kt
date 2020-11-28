package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.ext.RxSchedulers
import com.clipfinder.core.model.Paged
import com.clipfinder.core.model.Resource
import com.clipfinder.core.spotify.auth.ISpotifyAutoAuth
import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.clipfinder.core.usecase.SingleUseCaseWithArgs
import io.reactivex.Single

class GetPlaylistTracks(
    schedulers: RxSchedulers,
    private val auth: ISpotifyAutoAuth,
    private val repo: ISpotifyRepo
) : SingleUseCaseWithArgs<GetPlaylistTracks.Args, Resource<Paged<List<ISpotifyTrack>>>>(schedulers) {

    override fun run(args: Args): Single<Resource<Paged<List<ISpotifyTrack>>>> = auth
        .authorize()
        .andThen(repo.getPlaylistTracks(args.playlistId, args.offset))

    class Args(val playlistId: String, val offset: Int)
}
