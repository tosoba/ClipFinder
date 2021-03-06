package com.example.there.domain.usecase.videos

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.entity.videos.VideoPlaylistEntity
import com.example.there.domain.repo.videos.IVideosDbDataStore
import com.example.there.domain.usecase.base.CompletableUseCaseWithArgs
import com.example.there.domain.usecase.base.FlowableUseCaseWithArgs
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

class GetFavouriteVideosFromPlaylist @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val local: IVideosDbDataStore
) : FlowableUseCaseWithArgs<Long, List<VideoEntity>>(schedulersProvider) {
    override fun createFlowable(args: Long): Flowable<List<VideoEntity>> = local.getVideosFromPlaylist(args)
}

class AddVideoToPlaylist @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: IVideosDbDataStore
) : CompletableUseCaseWithArgs<AddVideoToPlaylist.Args>(schedulersProvider) {

    class Args(
            val playlistEntity: VideoPlaylistEntity,
            val videoEntity: VideoEntity
    )

    override fun createCompletable(args: Args): Completable = repository.addVideoToPlaylist(args.videoEntity, args.playlistEntity)
}

class DeleteVideo @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: IVideosDbDataStore
) : CompletableUseCaseWithArgs<VideoEntity>(schedulersProvider) {

    override fun createCompletable(args: VideoEntity): Completable = repository.deleteVideo(args)
}