package com.example.there.domain.usecase.spotify

import com.example.core.model.Resource
import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.Page
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetPlaylistsForCategory(
    schedulersProvider: UseCaseSchedulersProvider,
    private val remote: ISpotifyRemoteDataStore
) : SingleUseCaseWithArgs<GetPlaylistsForCategory.Args, Resource<Page<PlaylistEntity>>>(schedulersProvider) {

    class Args(val categoryId: String, val offset: Int)

    override fun run(
        args: Args
    ): Single<Resource<Page<PlaylistEntity>>> = remote.getPlaylistsForCategory(
        args.categoryId,
        args.offset
    )
}