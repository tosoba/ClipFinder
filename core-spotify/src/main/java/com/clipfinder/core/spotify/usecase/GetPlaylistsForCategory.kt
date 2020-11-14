package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.spotify.auth.ISpotifyAuth
import com.clipfinder.core.spotify.model.ISpotifySimplifiedPlaylist
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.example.core.ext.RxSchedulers
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetPlaylistsForCategory(
    schedulers: RxSchedulers,
    private val auth: ISpotifyAuth,
    private val repo: ISpotifyRepo
) : SingleUseCaseWithArgs<GetPlaylistsForCategory.Args, Resource<Paged<List<ISpotifySimplifiedPlaylist>>>>(schedulers) {

    override fun run(args: Args): Single<Resource<Paged<List<ISpotifySimplifiedPlaylist>>>> = auth
        .authorize()
        .andThen(repo.getPlaylistsForCategory(args.categoryId, args.offset))

    class Args(val categoryId: String, val offset: Int)
}