package com.example.there.domain.usecase.videos

import com.example.core.model.Resource
import com.example.core.ext.RxSchedulers
import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.repo.videos.IVideosRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetChannelsThumbnailUrls(
    schedulers: RxSchedulers,
    private val remote: IVideosRemoteDataStore
) : SingleUseCaseWithArgs<List<VideoEntity>, Resource<List<Pair<Int, String>>>>(schedulers) {
    override fun run(
        args: List<VideoEntity>
    ): Single<Resource<List<Pair<Int, String>>>> = remote.getChannelsThumbnailUrls(args)
}