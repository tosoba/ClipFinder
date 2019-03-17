package com.example.there.domain.usecase.spotify

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.spotify.SearchAllEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.base.SingleUseCaseWithInput
import io.reactivex.Single
import javax.inject.Inject

class SearchSpotify @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyRepository
) : SingleUseCaseWithInput<SearchSpotify.Input, SearchAllEntity>(schedulersProvider) {

    class Input(val query: String, val offset: Int)

    override fun createSingle(input: Input): Single<SearchAllEntity> = repository.searchAll(input.query, input.offset)
}