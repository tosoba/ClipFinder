package com.example.there.domain.usecase.videos

import com.example.core.ext.RxSchedulers
import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.entity.videos.VideoPlaylistEntity
import com.example.there.domain.repo.videos.IVideosDbDataStore
import com.example.there.domain.usecase.base.CompletableUseCaseWithArgs
import com.example.there.domain.usecase.base.FlowableUseCaseWithArgs
import io.reactivex.Completable
import io.reactivex.Flowable

class GetFavouriteVideosFromPlaylist(
    schedulers: RxSchedulers,
    private val local: IVideosDbDataStore
) : FlowableUseCaseWithArgs<Long, List<VideoEntity>>(schedulers) {
    override fun run(args: Long): Flowable<List<VideoEntity>> = local.getVideosFromPlaylist(args)
}

class AddVideoToPlaylist(
    schedulers: RxSchedulers,
    private val repository: IVideosDbDataStore
) : CompletableUseCaseWithArgs<AddVideoToPlaylist.Args>(schedulers) {

    class Args(
        val playlistEntity: VideoPlaylistEntity,
        val videoEntity: VideoEntity
    )

    override fun run(args: Args): Completable = repository.addVideoToPlaylist(args.videoEntity, args.playlistEntity)
}

class DeleteVideo(
    schedulers: RxSchedulers,
    private val repository: IVideosDbDataStore
) : CompletableUseCaseWithArgs<VideoEntity>(schedulers) {
    override fun run(args: VideoEntity): Completable = repository.deleteVideo(args)
}