package com.example.there.domain.usecase.spotify

import com.example.core.model.Resource
import com.example.core.ext.RxSchedulers
import com.example.there.domain.entity.Page
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetCurrentUsersPlaylists(
    schedulers: RxSchedulers,
    private val remote: ISpotifyRemoteDataStore
) : SingleUseCaseWithArgs<Int, Resource<Page<PlaylistEntity>>>(schedulers) {
    override fun run(args: Int): Single<Resource<Page<PlaylistEntity>>> = remote.getCurrentUsersPlaylists(offset = args)
}
