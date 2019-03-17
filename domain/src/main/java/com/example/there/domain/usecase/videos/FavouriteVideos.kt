package com.example.there.domain.usecase.videos

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.entity.videos.VideoPlaylistEntity
import com.example.there.domain.repo.videos.IVideosRepository
import com.example.there.domain.usecase.base.CompletableUseCaseWithInput
import com.example.there.domain.usecase.base.FlowableUseCaseWithInput
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

class GetFavouriteVideosFromPlaylist @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: IVideosRepository
) : FlowableUseCaseWithInput<Long, List<VideoEntity>>(schedulersProvider) {

    override fun createFlowable(input: Long): Flowable<List<VideoEntity>> = repository.getVideosFromPlaylist(input)
}

class AddVideoToPlaylist @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: IVideosRepository
) : CompletableUseCaseWithInput<AddVideoToPlaylist.Input>(schedulersProvider) {

    class Input(
            val playlistEntity: VideoPlaylistEntity,
            val videoEntity: VideoEntity
    )

    override fun createCompletable(input: Input): Completable = repository.addVideoToPlaylist(input.videoEntity, input.playlistEntity)
}

class DeleteVideo @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: IVideosRepository
) : CompletableUseCaseWithInput<VideoEntity>(schedulersProvider) {

    override fun createCompletable(input: VideoEntity): Completable = repository.deleteVideo(input)
}