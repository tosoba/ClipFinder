package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.model.Paged
import com.clipfinder.core.model.Resource
import com.clipfinder.core.model.UseCaseWithArgs
import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import io.reactivex.Single

class GetPlaylistTracks(private val repo: ISpotifyRepo) :
    UseCaseWithArgs<GetPlaylistTracks.Args, Single<Resource<Paged<List<ISpotifyTrack>>>>> {
    override fun run(args: Args): Single<Resource<Paged<List<ISpotifyTrack>>>> =
        repo.getPlaylistTracks(args.playlistId, args.offset)

    class Args(val playlistId: String, val offset: Int)
}
