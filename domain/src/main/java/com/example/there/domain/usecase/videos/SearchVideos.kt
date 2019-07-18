package com.example.there.domain.usecase.videos

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.repo.videos.IVideosDbDataStore
import com.example.there.domain.repo.videos.IVideosRemoteDataStore
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class SearchVideos(
        schedulersProvider: UseCaseSchedulersProvider,
        private val remote: IVideosRemoteDataStore,
        private val local: IVideosDbDataStore
) : SingleUseCaseWithArgs<SearchVideos.Args, List<VideoEntity>>(schedulersProvider) {

    class Args(val query: String, val loadMore: Boolean)

    override fun run(args: Args): Single<List<VideoEntity>> = if (args.loadMore)
        getMoreVideos(args.query)
    else getVideos(args.query)

    private fun getVideos(
            query: String
    ): Single<List<VideoEntity>> = local.getSavedVideosForQuery(query)
            .flatMap {
                if (it.isEmpty()) {
                    remote.getVideos(query)
                            .flatMap { (nextPageToken, videos) ->
                                local.insertVideosForNewQuery(query, videos, nextPageToken)
                                        .andThen(Single.just(videos))
                            }
                } else {
                    Single.just(it)
                }
            }

    private fun getMoreVideos(
            query: String
    ): Single<List<VideoEntity>> = local.getNextPageTokenForQuery(query)
            .flatMapSingle { remote.getVideos(query, it) }
            .flatMap { (nextPageToken, videos) ->
                local.insertVideosForQuery(query, videos, nextPageToken)
                        .andThen(Single.just(videos))
            }
}