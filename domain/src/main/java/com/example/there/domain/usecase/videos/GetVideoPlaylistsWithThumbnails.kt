package com.example.there.domain.usecase.videos

import com.example.there.domain.entity.videos.VideoPlaylistThumbnailsEntity
import com.example.there.domain.repo.videos.IVideosRepository
import com.example.there.domain.usecase.base.FlowableUseCase
import io.reactivex.Flowable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

class GetVideoPlaylistsWithThumbnails @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: IVideosRepository
) : FlowableUseCase<VideoPlaylistThumbnailsEntity>(subscribeOnScheduler, observeOnScheduler) {

    override val flowable: Flowable<VideoPlaylistThumbnailsEntity>
        get() = repository.videoPlaylistsWithThumbnails
}