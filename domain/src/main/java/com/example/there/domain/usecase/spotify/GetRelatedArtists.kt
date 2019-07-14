package com.example.there.domain.usecase.spotify

import com.example.core.model.Resource
import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single
import javax.inject.Inject

class GetRelatedArtists @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val remote: ISpotifyRemoteDataStore
) : SingleUseCaseWithArgs<String, Resource<List<ArtistEntity>>>(schedulersProvider) {
    override fun run(args: String): Single<Resource<List<ArtistEntity>>> = remote.getRelatedArtists(args)
}