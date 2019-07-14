package com.example.there.domain.usecase.videos

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.repo.videos.IVideosRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single
import javax.inject.Inject

class GetChannelsThumbnailUrls @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val remote: IVideosRemoteDataStore
) : SingleUseCaseWithArgs<List<VideoEntity>, List<Pair<Int, String>>>(schedulersProvider) {

    override fun run(args: List<VideoEntity>): Single<List<Pair<Int, String>>> = remote.getChannelsThumbnailUrls(args)
}