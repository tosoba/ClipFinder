package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.ext.RxSchedulers
import com.clipfinder.core.model.Paged
import com.clipfinder.core.model.Resource
import com.clipfinder.core.spotify.auth.ISpotifyAutoAuth
import com.clipfinder.core.spotify.model.ISpotifySimplifiedPlaylist
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.clipfinder.core.usecase.SingleUseCaseWithArgs
import io.reactivex.Single

class GetPlaylistsForCategory(
    schedulers: RxSchedulers,
    private val auth: ISpotifyAutoAuth,
    private val repo: ISpotifyRepo
) : SingleUseCaseWithArgs<GetPlaylistsForCategory.Args, Resource<Paged<List<ISpotifySimplifiedPlaylist>>>>(schedulers) {

    override fun run(args: Args): Single<Resource<Paged<List<ISpotifySimplifiedPlaylist>>>> = auth
        .authorize()
        .andThen(repo.getPlaylistsForCategory(args.categoryId, args.offset))

    class Args(val categoryId: String, val offset: Int)
}
