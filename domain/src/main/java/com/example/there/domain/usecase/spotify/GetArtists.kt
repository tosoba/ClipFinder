package com.example.there.domain.usecase.spotify

import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.base.SingleUseCaseWithInput
import io.reactivex.Scheduler
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class GetArtists @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: ISpotifyRepository
) : SingleUseCaseWithInput<List<String>, List<ArtistEntity>>(subscribeOnScheduler, observeOnScheduler) {

    override fun createSingle(input: List<String>): Single<List<ArtistEntity>> = repository.getArtists(artistIds = input)
}