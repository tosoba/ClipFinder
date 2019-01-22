package com.example.there.domain.usecase.videos

import com.example.there.domain.common.SymmetricFlowableTransformer
import com.example.there.domain.entity.videos.VideoPlaylistThumbnailsEntity
import com.example.there.domain.repo.videos.IVideosRepository
import com.example.there.domain.usecase.base.FlowableUseCase
import io.reactivex.Flowable

class GetVideoPlaylistsWithThumbnails(
        transformer: SymmetricFlowableTransformer<VideoPlaylistThumbnailsEntity>,
        private val repository: IVideosRepository
) : FlowableUseCase<VideoPlaylistThumbnailsEntity>(transformer) {

    override fun createFlowable(
            data: Map<String, Any?>?
    ): Flowable<VideoPlaylistThumbnailsEntity> = repository.videoPlaylistsWithThumbnails
}