package com.example.there.domain.usecase.videos

import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.repo.videos.IVideosRepository
import com.example.there.domain.usecase.base.SingleUseCaseWithInput
import io.reactivex.Scheduler
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class GetChannelsThumbnailUrls @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: IVideosRepository
) : SingleUseCaseWithInput<List<VideoEntity>, List<Pair<Int, String>>>(subscribeOnScheduler, observeOnScheduler) {

    override fun createSingle(input: List<VideoEntity>): Single<List<Pair<Int, String>>> = repository.getChannelsThumbnailUrls(input)
}