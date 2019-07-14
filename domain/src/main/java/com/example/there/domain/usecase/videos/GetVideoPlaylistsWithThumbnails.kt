package com.example.there.domain.usecase.videos

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.videos.VideoPlaylistThumbnailsEntity
import com.example.there.domain.repo.videos.IVideosDbDataStore
import com.example.there.domain.usecase.base.FlowableUseCase
import io.reactivex.Flowable
import javax.inject.Inject

class GetVideoPlaylistsWithThumbnails @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val local: IVideosDbDataStore
) : FlowableUseCase<VideoPlaylistThumbnailsEntity>(schedulersProvider) {

    override val result: Flowable<VideoPlaylistThumbnailsEntity>
        get() = local.videoPlaylistsWithThumbnails
}