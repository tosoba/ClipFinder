package com.example.there.domain.usecase.videos

import com.example.core.model.Resource
import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.repo.videos.IVideosDbDataStore
import com.example.there.domain.repo.videos.IVideosRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class SearchRelatedVideos(
        schedulersProvider: UseCaseSchedulersProvider,
        private val remote: IVideosRemoteDataStore,
        private val local: IVideosDbDataStore
) : SingleUseCaseWithArgs<SearchRelatedVideos.Args, Resource<List<VideoEntity>>>(schedulersProvider) {

    class Args(val videoId: String, val loadMore: Boolean)

    override fun run(args: Args): Single<Resource<List<VideoEntity>>> = if (args.loadMore)
        getMoreRelatedVideos(args.videoId)
    else getRelatedVideos(args.videoId)

    private fun getRelatedVideos(
            videoId: String
    ): Single<Resource<List<VideoEntity>>> = local.getRelatedVideosForVideoId(videoId)
            .flatMap { savedVideos ->
                if (savedVideos.isEmpty()) {
                    remote.getRelatedVideos(videoId)
                            .flatMap { resource ->
                                when (resource) {
                                    is Resource.Success -> {
                                        val (nextPageToken, videos) = resource.data
                                        local.insertRelatedVideosForNewVideoId(videoId, videos, nextPageToken)
                                                .andThen(Single.just(Resource.Success(videos)))
                                    }
                                    is Resource.Error<Pair<String?, List<VideoEntity>>, *> ->
                                        Single.just(resource.map { it.second })
                                }
                            }
                } else {
                    Single.just(Resource.Success(savedVideos))
                }
            }

    private fun getMoreRelatedVideos(
            videoId: String
    ): Single<Resource<List<VideoEntity>>> = local.getNextPageTokenForVideoId(videoId)
            .flatMapSingle { remote.getRelatedVideos(videoId, it) }
            .flatMap { resource: Resource<Pair<String?, List<VideoEntity>>> ->
                when (resource) {
                    is Resource.Success -> {
                        val (nextPageToken, videos) = resource.data
                        local.insertRelatedVideosForVideoId(videoId, videos, nextPageToken)
                                .andThen(Single.just(Resource.Success(videos)))
                    }
                    is Resource.Error<Pair<String?, List<VideoEntity>>, *> ->
                        Single.just(resource.map { it.second })
                }
            }
}