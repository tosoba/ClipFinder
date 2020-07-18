package com.example.there.domain.usecase.spotify

import com.example.core.model.Resource
import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.Page
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetCurrentUsersSavedAlbums(
    schedulersProvider: UseCaseSchedulersProvider,
    private val remote: ISpotifyRemoteDataStore
) : SingleUseCaseWithArgs<Int, Resource<Page<AlbumEntity>>>(schedulersProvider) {
    override fun run(args: Int): Single<Resource<Page<AlbumEntity>>> = remote.getCurrentUsersSavedAlbums(offset = args)
}
