package com.example.there.domain.usecase.spotify

import com.example.there.domain.entity.EntityPage
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.base.SingleUseCaseWithInput
import io.reactivex.Scheduler
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class GetPlaylistsForCategory @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: ISpotifyRepository
) : SingleUseCaseWithInput<GetPlaylistsForCategory.Input, EntityPage<PlaylistEntity>>(subscribeOnScheduler, observeOnScheduler) {

    class Input(
            val categoryId: String,
            val offset: Int
    )

    override fun createSingle(input: Input): Single<EntityPage<PlaylistEntity>> = repository.getPlaylistsForCategory(input.categoryId, input.offset)
}