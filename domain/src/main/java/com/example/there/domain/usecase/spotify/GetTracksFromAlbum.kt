package com.example.there.domain.usecase.spotify

import com.example.there.domain.entity.EntityPage
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.base.ObservableUseCaseWithInput
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

class GetTracksFromAlbum @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: ISpotifyRepository
) : ObservableUseCaseWithInput<String, EntityPage<TrackEntity>>(subscribeOnScheduler, observeOnScheduler) {

    override fun createObservable(input: String): Observable<EntityPage<TrackEntity>> = repository.getTracksFromAlbum(input)
}