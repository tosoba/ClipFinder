package com.example.there.domain.usecase.videos

import com.example.core.ext.RxSchedulers
import com.example.core.model.Resource
import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.repo.videos.IVideosRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class SearchRelatedVideos(
    schedulers: RxSchedulers,
    private val remote: IVideosRemoteDataStore
) : SingleUseCaseWithArgs<SearchRelatedVideos.Args, Resource<List<VideoEntity>>>(schedulers) {

    class Args(val videoId: String, val loadMore: Boolean)

    override fun run(args: Args): Single<Resource<List<VideoEntity>>> = if (args.loadMore) {
        getMoreRelatedVideos(args.videoId)
    } else {
        getRelatedVideos(args.videoId)
    }

    private fun getRelatedVideos(videoId: String): Single<Resource<List<VideoEntity>>> = remote
        .getRelatedVideos(videoId)
        .map { resource ->
            when (resource) {
                is Resource.Success -> {
                    val (_, videos) = resource.data
                    Resource.Success(videos)
                }
                is Resource.Error<Pair<String?, List<VideoEntity>>> -> resource.map { it.second }
            }
        }

    private fun getMoreRelatedVideos(videoId: String): Single<Resource<List<VideoEntity>>> = Single
        .just(Resource.Success(emptyList()))
}
