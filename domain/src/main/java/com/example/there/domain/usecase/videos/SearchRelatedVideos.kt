package com.example.there.domain.usecase.videos

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.repo.videos.IVideosRepository
import com.example.there.domain.usecase.base.SingleUseCaseWithInput
import io.reactivex.Single
import javax.inject.Inject

class SearchRelatedVideos @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: IVideosRepository
) : SingleUseCaseWithInput<SearchRelatedVideos.Input, List<VideoEntity>>(schedulersProvider) {

    class Input(
            val videoId: String,
            val loadMore: Boolean
    )

    override fun createSingle(input: Input): Single<List<VideoEntity>> = if (input.loadMore)
        repository.getMoreRelatedVideos(input.videoId)
    else repository.getRelatedVideos(input.videoId)
}