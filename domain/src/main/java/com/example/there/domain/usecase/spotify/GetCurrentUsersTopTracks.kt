package com.example.there.domain.usecase.spotify

import com.example.there.domain.entity.EntityPage
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.base.SingleUseCaseWithInput
import io.reactivex.Scheduler
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class GetCurrentUsersTopTracks @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: ISpotifyRepository
) : SingleUseCaseWithInput<Int, EntityPage<TrackEntity>>(subscribeOnScheduler, observeOnScheduler) {

    override fun createSingle(input: Int): Single<EntityPage<TrackEntity>> = repository.getCurrentUsersTopTracks(offset = input)
}