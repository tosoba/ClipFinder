package com.example.there.domain.usecase.spotify

import com.example.core.model.Resource
import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.Page
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetCurrentUsersTopArtists(
    schedulersProvider: UseCaseSchedulersProvider,
    private val remote: ISpotifyRemoteDataStore
) : SingleUseCaseWithArgs<Int, Resource<Page<ArtistEntity>>>(schedulersProvider) {
    override fun run(args: Int): Single<Resource<Page<ArtistEntity>>> = remote.getCurrentUsersTopArtists(offset = args)
}
