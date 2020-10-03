package com.example.there.domain.usecase.videos

import com.example.core.ext.RxSchedulers
import com.example.core.model.Resource
import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.repo.videos.IVideosRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class SearchVideos(
    schedulers: RxSchedulers,
    private val remote: IVideosRemoteDataStore
) : SingleUseCaseWithArgs<SearchVideos.Args, Resource<List<VideoEntity>>>(schedulers) {

    class Args(val query: String, val loadMore: Boolean)

    override fun run(args: Args): Single<Resource<List<VideoEntity>>> = if (args.loadMore) {
        getMoreVideos(args.query)
    } else {
        getVideos(args.query)
    }

    private fun getVideos(query: String): Single<Resource<List<VideoEntity>>> = remote
        .getVideos(query)
        .map { resource ->
            when (resource) {
                is Resource.Success -> {
                    val (_, videos) = resource.data
                    Resource.Success(videos)
                }
                is Resource.Error<Pair<String?, List<VideoEntity>>, *> -> resource.map { it.second }
            }
        }

    private fun getMoreVideos(query: String): Single<Resource<List<VideoEntity>>> = Single
        .just(Resource.Success(emptyList()))
}
