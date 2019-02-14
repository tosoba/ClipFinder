package com.example.there.domain.usecase.spotify

import com.example.there.domain.entity.spotify.TopTrackEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.base.ObservableUseCase
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

class GetDailyViralTracks @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: ISpotifyRepository
) : ObservableUseCase<List<TopTrackEntity>>(subscribeOnScheduler, observeOnScheduler) {

    override val observable: Observable<List<TopTrackEntity>>
        get() = repository.dailyViralTracks
}