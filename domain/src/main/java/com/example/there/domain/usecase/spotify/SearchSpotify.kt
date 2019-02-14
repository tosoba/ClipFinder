package com.example.there.domain.usecase.spotify

import com.example.there.domain.entity.spotify.SearchAllEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.base.SingleUseCaseWithInput
import io.reactivex.Scheduler
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class SearchSpotify @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: ISpotifyRepository
) : SingleUseCaseWithInput<SearchSpotify.Input, SearchAllEntity>(subscribeOnScheduler, observeOnScheduler) {

    class Input(val query: String, val offset: Int)

    override fun createSingle(input: Input): Single<SearchAllEntity> = repository.searchAll(input.query, input.offset)
}