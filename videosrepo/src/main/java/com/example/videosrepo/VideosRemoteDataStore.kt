package com.example.videosrepo

import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.repo.videos.IVideosRemoteDataStore
import com.example.videosrepo.mapper.domain
import com.example.videosrepo.util.urlMedium
import com.example.youtubeapi.YoutubeApi
import com.example.youtubeapi.model.VideoApiModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideosRemoteDataStore @Inject constructor(
        private val api: YoutubeApi
) : IVideosRemoteDataStore {

    override fun getChannelsThumbnailUrls(
            videos: List<VideoEntity>
    ): Single<List<Pair<Int, String>>> = Observable.fromIterable(videos.chunked(50))
            .zipWith(
                    Observable.range(0, Int.MAX_VALUE),
                    BiFunction<List<VideoEntity>, Int, Pair<Int, List<VideoEntity>>> { vids, chunkIndex ->
                        Pair(chunkIndex, vids)
                    }
            )
            .flatMap { (chunkIndex, vids) ->
                api.loadChannelsInfo(ids = vids.joinToString(",") { it.channelId })
                        .toObservable()
                        .map { response -> response.channels.map { it.snippet.thumbnails.urlMedium } }
                        .concatMapIterable { it }
                        .zipWith(
                                Observable.range(0, Int.MAX_VALUE),
                                BiFunction<String, Int, Pair<Int, String>> { url, urlIndex ->
                                    Pair(urlIndex + 50 * chunkIndex, url)
                                }
                        )
            }
            .toList()

    override fun getVideos(
            query: String,
            pageToken: String?
    ): Single<Pair<String?, List<VideoEntity>>> = api.searchVideos(query = query, pageToken = pageToken)
            .flatMap { searchResponse ->
                api.loadVideosInfo(ids = searchResponse.videos.joinToString(",") { it.id.id })
                        .map { it.videos.map(VideoApiModel::domain) }
                        .map { Pair(searchResponse.nextPageToken, it) }
            }

    override fun getRelatedVideos(
            toVideoId: String,
            pageToken: String?
    ): Single<Pair<String?, List<VideoEntity>>> = api.searchRelatedVideos(toVideoId = toVideoId, pageToken = pageToken)
            .flatMap { searchResponse ->
                api.loadVideosInfo(ids = searchResponse.videos.joinToString(",") { it.id.id })
                        .map { it.videos.map(VideoApiModel::domain) }
                        .map { Pair(searchResponse.nextPageToken, it) }
            }
}