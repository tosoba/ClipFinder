package com.example.there.domain.usecase.videos

import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.repo.videos.IVideosRepository
import com.example.there.domain.usecase.base.SingleUseCaseWithInput
import io.reactivex.Scheduler
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class SearchVideos @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: IVideosRepository
) : SingleUseCaseWithInput<SearchVideos.Input, List<VideoEntity>>(subscribeOnScheduler, observeOnScheduler) {

    class Input(
            val query: String,
            val loadMore: Boolean
    )

    override fun createSingle(input: Input): Single<List<VideoEntity>> = if (input.loadMore)
        repository.getMoreVideos(input.query)
    else repository.getVideos(input.query)
}