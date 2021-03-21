package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.model.Paged
import com.clipfinder.core.model.Resource
import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.clipfinder.core.model.UseCaseWithArgs
import io.reactivex.Single

class GetTracksFromAlbum(
    private val repo: ISpotifyRepo
) : UseCaseWithArgs<GetTracksFromAlbum.Args, Single<Resource<Paged<List<ISpotifyTrack>>>>> {
    override fun run(args: Args): Single<Resource<Paged<List<ISpotifyTrack>>>> = repo
        .getTracksFromAlbum(albumId = args.albumId, offset = args.offset)

    class Args(val albumId: String, val offset: Int)
}
