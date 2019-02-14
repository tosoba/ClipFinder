package com.example.there.domain.usecase.spotify

import com.example.there.domain.entity.EntityPage
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.base.SingleUseCaseWithInput
import io.reactivex.Scheduler
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class GetPlaylistTracks @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: ISpotifyRepository
) : SingleUseCaseWithInput<GetPlaylistTracks.Input, EntityPage<TrackEntity>>(subscribeOnScheduler, observeOnScheduler) {

    class Input(
            val playlistId: String,
            val userId: String,
            val offset: Int
    )

    override fun createSingle(input: Input): Single<EntityPage<TrackEntity>> = repository.getPlaylistTracks(input.playlistId, input.userId, input.offset)
}