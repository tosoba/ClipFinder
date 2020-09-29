package com.example.spotify.category.domain.usecase

import com.clipfinder.core.spotify.auth.ISpotifyAuth
import com.clipfinder.core.spotify.model.ISpotifySimplePlaylist
import com.example.core.ext.RxSchedulers
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.spotify.category.domain.repo.ISpotifyCategoryRepo
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetPlaylistsForCategory(
    schedulers: RxSchedulers,
    private val auth: ISpotifyAuth,
    private val repo: ISpotifyCategoryRepo
) : SingleUseCaseWithArgs<GetPlaylistsForCategory.Args, Resource<Paged<List<ISpotifySimplePlaylist>>>>(schedulers) {

    override fun run(args: Args): Single<Resource<Paged<List<ISpotifySimplePlaylist>>>> = auth
        .authorize()
        .andThen(repo.getPlaylistsForCategory(args.categoryId, args.offset))

    class Args(val categoryId: String, val offset: Int)
}
