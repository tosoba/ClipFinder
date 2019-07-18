package com.example.there.domain.usecase.spotify

import com.example.core.model.Resource
import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.spotify.SearchAllEntity
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class SearchSpotify(
        schedulersProvider: UseCaseSchedulersProvider,
        private val remote: ISpotifyRemoteDataStore
) : SingleUseCaseWithArgs<SearchSpotify.Args, Resource<SearchAllEntity>>(schedulersProvider) {

    class Args(val query: String, val offset: Int)

    override fun run(args: Args): Single<Resource<SearchAllEntity>> = remote.searchAll(args.query, args.offset)
}