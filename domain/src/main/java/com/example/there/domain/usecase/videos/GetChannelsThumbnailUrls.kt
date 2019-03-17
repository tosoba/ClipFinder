package com.example.there.domain.usecase.videos

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.repo.videos.IVideosRepository
import com.example.there.domain.usecase.base.SingleUseCaseWithInput
import io.reactivex.Single
import javax.inject.Inject

class GetChannelsThumbnailUrls @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: IVideosRepository
) : SingleUseCaseWithInput<List<VideoEntity>, List<Pair<Int, String>>>(schedulersProvider) {

    override fun createSingle(input: List<VideoEntity>): Single<List<Pair<Int, String>>> = repository.getChannelsThumbnailUrls(input)
}