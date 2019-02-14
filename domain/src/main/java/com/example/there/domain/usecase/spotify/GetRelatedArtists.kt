package com.example.there.domain.usecase.spotify

import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.base.SingleUseCaseWithInput
import io.reactivex.Scheduler
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class GetRelatedArtists @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: ISpotifyRepository
) : SingleUseCaseWithInput<String, List<ArtistEntity>>(subscribeOnScheduler, observeOnScheduler) {

    override fun createSingle(input: String): Single<List<ArtistEntity>> = repository.getRelatedArtists(input)
}