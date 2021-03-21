package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.model.Paged
import com.clipfinder.core.model.Resource
import com.clipfinder.core.spotify.model.ISpotifySimplifiedPlaylist
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.clipfinder.core.model.UseCaseWithArgs
import io.reactivex.Single

class GetPlaylistsForCategory(
    private val repo: ISpotifyRepo
) : UseCaseWithArgs<GetPlaylistsForCategory.Args, Single<Resource<Paged<List<ISpotifySimplifiedPlaylist>>>>> {
    override fun run(args: Args): Single<Resource<Paged<List<ISpotifySimplifiedPlaylist>>>> = repo
        .getPlaylistsForCategory(args.categoryId, args.offset)

    class Args(val categoryId: String, val offset: Int)
}
