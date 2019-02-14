package com.example.there.domain.usecase.videos

import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.entity.videos.VideoPlaylistEntity
import com.example.there.domain.repo.videos.IVideosRepository
import com.example.there.domain.usecase.base.CompletableUseCaseWithInput
import com.example.there.domain.usecase.base.FlowableUseCaseWithInput
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

class GetFavouriteVideosFromPlaylist @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: IVideosRepository
) : FlowableUseCaseWithInput<Long, List<VideoEntity>>(subscribeOnScheduler, observeOnScheduler) {

    override fun createFlowable(input: Long): Flowable<List<VideoEntity>> = repository.getVideosFromPlaylist(input)
}

class AddVideoToPlaylist @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: IVideosRepository
) : CompletableUseCaseWithInput<AddVideoToPlaylist.Input>(subscribeOnScheduler, observeOnScheduler) {

    class Input(
            val playlistEntity: VideoPlaylistEntity,
            val videoEntity: VideoEntity
    )

    override fun createCompletable(input: Input): Completable = repository.addVideoToPlaylist(input.videoEntity, input.playlistEntity)
}

class DeleteVideo @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: IVideosRepository
) : CompletableUseCaseWithInput<VideoEntity>(subscribeOnScheduler, observeOnScheduler) {

    override fun createCompletable(input: VideoEntity): Completable = repository.deleteVideo(input)
}