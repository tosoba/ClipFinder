package com.example.there.domain.usecase.spotify

import com.example.core.model.Resource
import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.Page
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetCurrentUsersPlaylists(
    schedulersProvider: UseCaseSchedulersProvider,
    private val remote: ISpotifyRemoteDataStore
) : SingleUseCaseWithArgs<Int, Resource<Page<PlaylistEntity>>>(schedulersProvider) {
    override fun run(args: Int): Single<Resource<Page<PlaylistEntity>>> = remote.getCurrentUsersPlaylists(offset = args)
}
