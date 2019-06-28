package com.example.there.domain.usecase.spotify

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.spotify.SearchAllEntity
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single
import javax.inject.Inject

class SearchSpotify @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val remote: ISpotifyRemoteDataStore
) : SingleUseCaseWithArgs<SearchSpotify.Args, SearchAllEntity>(schedulersProvider) {

    class Args(val query: String, val offset: Int)

    override fun createSingle(args: Args): Single<SearchAllEntity> = remote.searchAll(args.query, args.offset)
}