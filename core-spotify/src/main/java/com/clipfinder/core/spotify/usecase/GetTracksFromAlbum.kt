package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.spotify.auth.ISpotifyAutoAuth
import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.example.core.ext.RxSchedulers
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetTracksFromAlbum(
    schedulers: RxSchedulers,
    private val auth: ISpotifyAutoAuth,
    private val repo: ISpotifyRepo
) : SingleUseCaseWithArgs<GetTracksFromAlbum.Args, Resource<Paged<List<ISpotifyTrack>>>>(schedulers) {

    override fun run(args: Args): Single<Resource<Paged<List<ISpotifyTrack>>>> = auth
        .authorize()
        .andThen(repo.getTracksFromAlbum(albumId = args.albumId, offset = args.offset))

    class Args(val albumId: String, val offset: Int)
}
