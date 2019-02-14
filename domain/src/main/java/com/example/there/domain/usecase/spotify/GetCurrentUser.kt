package com.example.there.domain.usecase.spotify

import com.example.there.domain.entity.spotify.UserEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Scheduler
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class GetCurrentUser @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: ISpotifyRepository
) : SingleUseCase<UserEntity>(subscribeOnScheduler, observeOnScheduler) {

    override val single: Single<UserEntity>
        get() = repository.currentUser
}