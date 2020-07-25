package com.example.there.domain.usecase.videos

import com.example.core.ext.RxSchedulers
import com.example.core.model.Resource
import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.repo.videos.IVideosDbDataStore
import com.example.there.domain.repo.videos.IVideosRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class SearchVideos(
    schedulers: RxSchedulers,
    private val remote: IVideosRemoteDataStore,
    private val local: IVideosDbDataStore
) : SingleUseCaseWithArgs<SearchVideos.Args, Resource<List<VideoEntity>>>(schedulers) {

    class Args(val query: String, val loadMore: Boolean)

    override fun run(args: Args): Single<Resource<List<VideoEntity>>> = if (args.loadMore) {
        getMoreVideos(args.query)
    } else {
        getVideos(args.query)
    }

    private fun getVideos(
        query: String
    ): Single<Resource<List<VideoEntity>>> = local.getSavedVideosForQuery(query)
        .flatMap { savedVideos ->
            if (savedVideos.isEmpty()) {
                remote.getVideos(query).flatMap { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val (nextPageToken, videos) = resource.data
                            local.insertVideosForNewQuery(query, videos, nextPageToken)
                                .andThen(Single.just(Resource.Success(videos)))
                        }
                        is Resource.Error<Pair<String?, List<VideoEntity>>, *> -> Single
                            .just(resource.map { it.second })
                    }
                }
            } else {
                Single.just(Resource.Success(savedVideos))
            }
        }

    private fun getMoreVideos(
        query: String
    ): Single<Resource<List<VideoEntity>>> = local.getNextPageTokenForQuery(query)
        .flatMapSingle { remote.getVideos(query, it) }
        .flatMap { resource: Resource<Pair<String?, List<VideoEntity>>> ->
            when (resource) {
                is Resource.Success -> {
                    val (nextPageToken, videos) = resource.data
                    local.insertVideosForQuery(query, videos, nextPageToken)
                        .andThen(Single.just(Resource.Success(videos)))
                }
                is Resource.Error<Pair<String?, List<VideoEntity>>, *> ->
                    Single.just(resource.map { it.second })
            }
        }
}
