package com.example.there.domain.usecase.videos

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.repo.videos.IVideosDbDataStore
import com.example.there.domain.repo.videos.IVideosRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single
import javax.inject.Inject

class SearchRelatedVideos @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val remote: IVideosRemoteDataStore,
        private val local: IVideosDbDataStore
) : SingleUseCaseWithArgs<SearchRelatedVideos.Args, List<VideoEntity>>(schedulersProvider) {

    class Args(val videoId: String, val loadMore: Boolean)

    override fun createSingle(args: Args): Single<List<VideoEntity>> = if (args.loadMore)
        getMoreRelatedVideos(args.videoId)
    else getRelatedVideos(args.videoId)

    private fun getRelatedVideos(
            videoId: String
    ): Single<List<VideoEntity>> = local.getRelatedVideosForVideoId(videoId)
            .flatMap {
                if (it.isEmpty()) {
                    remote.getRelatedVideos(videoId)
                            .flatMap { (nextPageToken, videos) ->
                                local.insertRelatedVideosForNewVideoId(videoId, videos, nextPageToken)
                                        .andThen(Single.just(videos))
                            }
                } else {
                    Single.just(it)
                }
            }

    private fun getMoreRelatedVideos(
            videoId: String
    ): Single<List<VideoEntity>> = local.getNextPageTokenForVideoId(videoId)
            .flatMapSingle { remote.getRelatedVideos(videoId, it) }
            .flatMap { (nextPageToken, videos) ->
                local.insertRelatedVideosForVideoId(videoId, videos, nextPageToken)
                        .andThen(Single.just(videos))
            }
}