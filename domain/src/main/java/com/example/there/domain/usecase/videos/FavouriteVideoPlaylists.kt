package com.example.there.domain.usecase.videos

import com.example.there.domain.entity.videos.VideoPlaylistEntity
import com.example.there.domain.repo.videos.IVideosRepository
import com.example.there.domain.usecase.base.CompletableUseCaseWithInput
import com.example.there.domain.usecase.base.FlowableUseCase
import com.example.there.domain.usecase.base.SingleUseCaseWithInput
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class GetFavouriteVideoPlaylists @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: IVideosRepository
) : FlowableUseCase<List<VideoPlaylistEntity>>(subscribeOnScheduler, observeOnScheduler) {

    override val flowable: Flowable<List<VideoPlaylistEntity>>
        get() = repository.favouritePlaylists
}

class InsertVideoPlaylist @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: IVideosRepository
) : SingleUseCaseWithInput<VideoPlaylistEntity, Long>(subscribeOnScheduler, observeOnScheduler) {

    override fun createSingle(input: VideoPlaylistEntity): Single<Long> = repository.insertPlaylist(input)
}

class DeleteVideoPlaylist @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: IVideosRepository
) : CompletableUseCaseWithInput<VideoPlaylistEntity>(subscribeOnScheduler, observeOnScheduler) {

    override fun createCompletable(input: VideoPlaylistEntity): Completable = repository.deleteVideoPlaylist(input)
}