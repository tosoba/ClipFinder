package com.example.there.domain.usecase.videos

import com.example.core.ext.RxSchedulers
import com.example.there.domain.entity.videos.VideoPlaylistThumbnailsEntity
import com.example.there.domain.repo.videos.IVideosDbDataStore
import com.example.there.domain.usecase.base.FlowableUseCase
import io.reactivex.Flowable

class GetVideoPlaylistsWithThumbnails(
    schedulers: RxSchedulers,
    private val local: IVideosDbDataStore
) : FlowableUseCase<VideoPlaylistThumbnailsEntity>(schedulers) {
    override val result: Flowable<VideoPlaylistThumbnailsEntity>
        get() = local.videoPlaylistsWithThumbnails
}
